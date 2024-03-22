#!/bin/bash

# Clean any previous Dokka docs.
#rm -rf docs/api

# Build the Dokka docs.
#./gradlew clean :chat-sdk:dokka

# Copy outside files into the docs folder.
sed -e '/full configuration details and documentation here/ { N; d; }' < README.md > docs/introduction.md
cp botstacks-logo.png docs/

# Deploy to Github pages.
path/to/venv/bin/python3 -m mkdocs gh-deploy --force --verbose

# Clean up.
rm -rf docs/index.md docs/botstacks-logo.png
