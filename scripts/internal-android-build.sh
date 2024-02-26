#!/usr/bin/env bash

date="$(date '+%m.%d.%y')"

./gradlew assembleDebug

outputDir="$(pwd)/sample-android/build/outputs/apk/debug"
mv "${outputDir}/sample-android-debug.apk" "${outputDir}/sample-android-${date}-debug.apk"