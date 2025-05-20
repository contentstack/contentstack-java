package com.contentstack.sdk;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.files.FilesUploadV2Response;

public class SanityReport {

    private static final String PROPERTIES_FILE = "src/test/resources/.env";

    public void generateTestSummaryAndSendToSlack(File reportFile) throws IOException, SlackApiException {
        Properties properties = loadProperties(PROPERTIES_FILE);

        String slackToken = properties.getProperty("SLACK_BOT_TOKEN");
        String slackChannelID = properties.getProperty("SLACK_CHANNEL_ID");
        String signingSecret = properties.getProperty("SLACK_SIGNING_SECRET");
        String slackChannel = properties.getProperty("SLACK_CHANNEL");

        if (slackToken == null || slackChannelID == null) {
            System.err.println("Missing Slack credentials in properties.");
            return;
        }

        if (!reportFile.exists()) {
            System.err.println("Surefire report file not found at: " + reportFile.getAbsolutePath());
            return;
        }

        String message = generateTestSummary(reportFile);
        App app = configureSlackApp(slackToken, signingSecret);

        sendMessageToSlack(app, slackChannel, message);
        uploadReportToSlack(app, slackChannelID, reportFile);
    }

    private Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load properties: " + e.getMessage());
        }
        return properties;
    }

    private App configureSlackApp(String token, String secret) {
        App app = new App();
        app.config().setSigningSecret(secret);
        app.config().setSingleTeamBotToken(token);
        return app;
    }

    private void sendMessageToSlack(App app, String channel, String message) throws IOException, SlackApiException {
        ChatPostMessageResponse response = app.client().chatPostMessage(r -> r
                .channel(channel)
                .text(message)
        );
        if (response.isOk()) {
            System.out.println("Message sent successfully!");
        } else {
            System.err.println("Failed to send message: " + response.getError());
        }
    }

    private void uploadReportToSlack(App app, String channelID, File file) throws IOException, SlackApiException {
        FilesUploadV2Response response = app.client().filesUploadV2(fuvr -> fuvr
                .channel(channelID)
                .initialComment("Here is the report generated")
                .filename(file.getName())
                .file(file)
        );
        if (response.isOk()) {
            System.out.println("Report uploaded successfully!");
        } else {
            System.err.println("Failed to upload report: " + response.getError());
        }

    }

    private String generateTestSummary(File surefireReportFile) throws IOException {
        Document doc = Jsoup.parse(surefireReportFile, "UTF-8");
        Elements summaryRows = doc.select("table.table tr.b");
        Element summaryRow = summaryRows.first();

        int totalTests = 0, errors = 0, failures = 0, skipped = 0, passedTests, totalSuites, failedSuites = 0;
        String duration = "0m 0s";

        if (summaryRow != null) {
            Elements cells = summaryRow.select("td");
            if (cells.size() >= 6) {
                totalTests = Integer.parseInt(cells.get(0).text());
                errors = Integer.parseInt(cells.get(1).text());
                failures = Integer.parseInt(cells.get(2).text());
                skipped = Integer.parseInt(cells.get(3).text());

                String timeText = cells.get(5).text();
                if (timeText.contains("s")) {
                    double seconds = Double.parseDouble(timeText.replace(" s", ""));
                    duration = (int) seconds / 60 + "m " + (int) seconds % 60 + "s";
                }
            }
        }

        Elements testSuiteRows = doc.select("table:contains(Class) tr");
        totalSuites = testSuiteRows.size() - 1;

        for (Element row : testSuiteRows) {
            Elements errorCells = row.select("td:nth-child(4)");
            Elements failureCells = row.select("td:nth-child(5)");
            if (!errorCells.isEmpty() && !failureCells.isEmpty()) {
                try {
                    if (Integer.parseInt(errorCells.text()) > 0 || Integer.parseInt(failureCells.text()) > 0) {
                        failedSuites++;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        passedTests = totalTests - failures - errors - skipped;

        return "*Java CDA Test Report*\n"
                + "• Total Suites: " + totalSuites + "\n"
                + "• Total Tests: " + totalTests + "\n"
                + "• Passed Tests: " + passedTests + "\n"
                + "• Failed Suites: " + failedSuites + "\n"
                + "• Failed Tests: " + failures + "\n"
                + "• Skipped Tests: " + skipped + "\n"
                + "• Duration: " + duration;
    }

    public static void main(String[] args) {
        File reportFile = new File("target/reports/surefire.html");
        try {
            new SanityReport().generateTestSummaryAndSendToSlack(reportFile);
        } catch (IOException | SlackApiException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
