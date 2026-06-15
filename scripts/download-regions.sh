#!/usr/bin/env bash
# Download the latest regions.json from the Contentstack artifacts registry and
# write it to src/main/resources/assets/regions.json so it gets bundled into
# the SDK jar on the next build.
#
# Usage:
#   ./scripts/download-regions.sh
#   mvn exec:exec@refresh-regions
#
# Run this whenever Contentstack announces new regions or service keys, then
# commit the updated file:
#   git add src/main/resources/assets/regions.json
#   git commit -m "chore: refresh regions.json"

set -euo pipefail

REGIONS_URL="https://artifacts.contentstack.com/regions.json"
DEST="$(dirname "$0")/../src/main/resources/assets/regions.json"
DEST="$(cd "$(dirname "$DEST")" && pwd)/$(basename "$DEST")"

echo "Downloading regions.json from ${REGIONS_URL} ..."

if command -v curl &>/dev/null; then
    curl --silent --show-error --fail --location \
         --retry 3 --retry-delay 2 \
         -o "${DEST}" "${REGIONS_URL}"
elif command -v wget &>/dev/null; then
    wget --quiet --tries=3 --waitretry=2 -O "${DEST}" "${REGIONS_URL}"
else
    echo "Error: neither curl nor wget found. Install one and retry." >&2
    exit 1
fi

# Validate the downloaded file contains a "regions" array
if ! python3 -c "import sys, json; d=json.load(open('${DEST}')); assert 'regions' in d and len(d['regions']) > 0" 2>/dev/null &&
   ! python  -c "import sys, json; d=json.load(open('${DEST}')); assert 'regions' in d and len(d['regions']) > 0" 2>/dev/null; then
    # Fallback validation without Python — just check the key exists
    if ! grep -q '"regions"' "${DEST}"; then
        echo "Error: downloaded file does not look like a valid regions.json" >&2
        rm -f "${DEST}"
        exit 1
    fi
fi

REGION_COUNT=$(grep -o '"id"' "${DEST}" | wc -l | tr -d ' ')
echo "contentstack-java: regions.json updated (${REGION_COUNT} regions) → ${DEST}"
