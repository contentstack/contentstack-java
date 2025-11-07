#!/bin/bash

# Script to check JaCoCo coverage against thresholds
# Usage: ./check-coverage.sh

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Coverage thresholds
INSTRUCTION_THRESHOLD=90
BRANCH_THRESHOLD=80

echo "🔍 Checking JaCoCo coverage thresholds..."
echo ""

# Check if JaCoCo XML report exists
JACOCO_XML="target/site/jacoco/jacoco.xml"
if [ ! -f "$JACOCO_XML" ]; then
    echo -e "${RED}❌ JaCoCo report not found at $JACOCO_XML${NC}"
    echo "Please run: mvn clean test -Dtest='!*IT' jacoco:report -Dgpg.skip=true"
    exit 1
fi

# Extract coverage metrics from JaCoCo XML
# Using sed for cross-platform compatibility (macOS doesn't support grep -P)
INSTRUCTION_COVERED=$(sed -n 's/.*type="INSTRUCTION".*covered="\([0-9]*\)".*/\1/p' "$JACOCO_XML" | head -1)
INSTRUCTION_MISSED=$(sed -n 's/.*type="INSTRUCTION".*missed="\([0-9]*\)".*/\1/p' "$JACOCO_XML" | head -1)
BRANCH_COVERED=$(sed -n 's/.*type="BRANCH".*covered="\([0-9]*\)".*/\1/p' "$JACOCO_XML" | head -1)
BRANCH_MISSED=$(sed -n 's/.*type="BRANCH".*missed="\([0-9]*\)".*/\1/p' "$JACOCO_XML" | head -1)

# Calculate totals
INSTRUCTION_TOTAL=$((INSTRUCTION_COVERED + INSTRUCTION_MISSED))
BRANCH_TOTAL=$((BRANCH_COVERED + BRANCH_MISSED))

# Calculate percentages
if [ $INSTRUCTION_TOTAL -gt 0 ]; then
    INSTRUCTION_PCT=$((INSTRUCTION_COVERED * 100 / INSTRUCTION_TOTAL))
else
    INSTRUCTION_PCT=0
fi

if [ $BRANCH_TOTAL -gt 0 ]; then
    BRANCH_PCT=$((BRANCH_COVERED * 100 / BRANCH_TOTAL))
else
    BRANCH_PCT=0
fi

# Display coverage summary
echo "📊 Coverage Summary:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Instruction Coverage: $INSTRUCTION_PCT% ($INSTRUCTION_COVERED/$INSTRUCTION_TOTAL)"
echo "  Branch Coverage:      $BRANCH_PCT% ($BRANCH_COVERED/$BRANCH_TOTAL)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Check thresholds
THRESHOLD_MET=true

# Check instruction coverage
if [ $INSTRUCTION_PCT -ge $INSTRUCTION_THRESHOLD ]; then
    echo -e "${GREEN}✅ Instruction coverage ($INSTRUCTION_PCT%) meets threshold ($INSTRUCTION_THRESHOLD%)${NC}"
else
    echo -e "${RED}❌ Instruction coverage ($INSTRUCTION_PCT%) below threshold ($INSTRUCTION_THRESHOLD%)${NC}"
    THRESHOLD_MET=false
fi

# Check branch coverage
if [ $BRANCH_PCT -ge $BRANCH_THRESHOLD ]; then
    echo -e "${GREEN}✅ Branch coverage ($BRANCH_PCT%) meets threshold ($BRANCH_THRESHOLD%)${NC}"
else
    echo -e "${RED}❌ Branch coverage ($BRANCH_PCT%) below threshold ($BRANCH_THRESHOLD%)${NC}"
    THRESHOLD_MET=false
fi

echo ""

# Final result
if [ "$THRESHOLD_MET" = true ]; then
    echo -e "${GREEN}🎉 All coverage thresholds met!${NC}"
    echo ""
    echo "HTML report available at: target/site/jacoco/index.html"
    exit 0
else
    echo -e "${RED}💔 Coverage thresholds not met${NC}"
    echo ""
    echo "To improve coverage:"
    echo "  1. Review the HTML report: target/site/jacoco/index.html"
    echo "  2. Identify uncovered lines and branches"
    echo "  3. Add unit tests to cover the missing paths"
    echo ""
    exit 1
fi

