#!/bin/bash

echo "Running speed test... (using speedtest-cli)"
echo

# Run speedtest and store results
RESULT=$(speedtest-cli --simple)

DOWNLOAD=$(echo "$RESULT" | awk '/Download/ {printf("%.2f", $2/8)}')
UPLOAD=$(echo "$RESULT" | awk '/Upload/ {printf("%.2f", $2/8)}')

echo "======= Internet Speed ======="
echo "Download Speed : $DOWNLOAD MB/s"
echo "Upload Speed   : $UPLOAD MB/s"
echo "=============================="
