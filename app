#!/bin/bash

# Build the project quietly using Gradle.
# --quiet suppresses most build output.
# -x test skips the test task.
./gradlew build --quiet -x test

# Check if the build failed. If so, exit.
if [ $? -ne 0 ]; then
    echo "Build failed! Run './gradlew build' manually to see errors." >&2
    exit 1
fi

# Execute the application's JAR file using Java.
# Note the path is build/quarkus-app/ for Gradle.
# All arguments passed to this script ($@) are forwarded to the app.
java -Dquarkus.log.console.enable=false -jar cli/build/quarkus-app/quarkus-run.jar "$@"