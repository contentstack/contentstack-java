#!/bin/bash
# This script temporarily modifies the pom.xml file to enable tests,
# runs the tests, generates a Surefire HTML report, and sends it to Slack.
# It also ensures that the original pom.xml is restored afterward.
# Usage: ./send-report.sh
# Ensure the script is run from the root of the project
# macOS and Linux compatible
set -e  # Exit immediately if any command fails

# Create a temporary file that won't be committed
backup=$(mktemp)
# Function to restore pom.xml and clean up
restore_pom() {
  echo "🔄 Restoring original pom.xml..."
  cat "$backup" > pom.xml
  rm -f "$backup"  # Clean up our temp file
  echo "✅ Original pom.xml restored."
}
# Set trap to restore pom.xml on exit (normal or error)
trap restore_pom EXIT

echo "🔍 Backing up pom.xml..."
cat pom.xml > "$backup"

echo "🔧 Temporarily modifying pom.xml to enable tests..."
# Cross-platform sed command (works on both macOS and Linux)
if [[ "$OSTYPE" == "darwin"* ]]; then
  # macOS/BSD sed
  sed -i '' 's/<skipTests>true<\/skipTests>/<skipTests>false<\/skipTests>/g' pom.xml
else
  # GNU sed (Linux, including GoCD agents)
  sed -i 's/<skipTests>true<\/skipTests>/<skipTests>false<\/skipTests>/g' pom.xml
fi

echo "🔧 Building project..."
mvn clean package

echo "🧪 Running tests..."
mvn clean test

echo "📄 Generating Surefire HTML report..."
mvn surefire-report:report-only

echo "📤 Sending test report to Slack..."
mvn test-compile exec:java -Dexec.mainClass="com.contentstack.sdk.SanityReport" -Dexec.classpathScope=test

# Restore pom.xml and clean up
restore_pom
trap - EXIT  # Remove the trap

echo "✅ Done. All tests complete and original pom.xml restored."