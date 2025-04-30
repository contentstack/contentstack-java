#!/bin/bash

set -e  # Exit immediately if any command fails

echo "🧪 Running tests..."
mvn clean test

echo "📄 Generating Surefire HTML report..."
mvn surefire-report:report-only

echo "📤 Sending test report to Slack..."
mvn compile exec:java -Dexec.mainClass="com.contentstack.sdk.SanityReport"

echo "✅ Done."
