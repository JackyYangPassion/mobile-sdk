#!/usr/bin/env bash

function downloadSchema {
    echo "Downloading schema from $1"
    ./gradlew :sdk:downloadApolloSchema --endpoint="$1" --schema="$2"
    # Overwrite in-place to remove empty lines
    sed -i '' '/^$/d' "$2"
}

# Debug variant
downloadSchema 'https://chat.botstacks.ai/graphql' "$(pwd)/sdk/src/commonMain/graphql/schema.graphqls"
