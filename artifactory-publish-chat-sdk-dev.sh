#!/bin/bash
chmod +x ./gradlew
# artifactory publish for ripbull-chat-sdk
./gradlew :ripbull-chat-sdk:clean
./gradlew :ripbull-chat-sdk:assembleDevRelease -PparamEnv=dev
./gradlew :ripbull-chat-sdk:artifactoryPublish