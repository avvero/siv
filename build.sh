#!/usr/bin/env bash
set -ex

./gradlew jar

$GRAALVM_HOME/bin/native-image -cp ./build/libs/jpipe-1.0-SNAPSHOT.jar -H:Name=jpipe -H:Class=pw.avvero.hw.jpipe.App -H:+ReportUnsupportedElementsAtRuntime --allow-incomplete-classpath
