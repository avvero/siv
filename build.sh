#!/usr/bin/env bash
set -ex

./gradlew jar

$GRAALVM_HOME/bin/native-image -cp ./build/libs/siv-1.0-SNAPSHOT.jar -H:Name=siv -H:Class=pw.avvero.hw.siv.App -H:+ReportUnsupportedElementsAtRuntime --allow-incomplete-classpath
