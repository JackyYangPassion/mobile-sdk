#!/bin/bash
chmod +x ./gradlew
# artifactory publish for ripbull-chat-sdk
./gradlew :ripbull-chat-sdk:clean
./gradlew :ripbull-chat-sdk:assembleProdRelease -PparamEnv=prod
./gradlew :ripbull-chat-sdk:artifactoryPublish