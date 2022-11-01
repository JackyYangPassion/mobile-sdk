#!/bin/bash
chmod +x ./gradlew

# artifactory publish for sdk-cache
./gradlew :sdk-cache:clean
./gradlew :sdk-cache:assembleRelease
./gradlew :sdk-cache:artifactoryPublish

# artifactory publish for sdk-remote
./gradlew :sdk-remote:clean
./gradlew :sdk-remote:assembleRelease
./gradlew :sdk-remote:artifactoryPublish

# artifactory publish for sdk-mqtt
./gradlew :sdk-mqtt:clean
./gradlew :sdk-mqtt:assembleRelease
./gradlew :sdk-mqtt:artifactoryPublish

# artifactory publish for sdk-downloader
./gradlew :sdk-downloader:clean
./gradlew :sdk-downloader:assembleRelease
./gradlew :sdk-downloader:artifactoryPublish