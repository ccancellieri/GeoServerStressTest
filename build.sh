#!/bin/bash

# supported actions
PROFILES=""

# configure the command
CMD="mvn clean install eclipse:clean eclipse:eclipse \
 -DdownloadSources=true -DdownloadJavadocs=true -Declipse.addVersionToProjectName=true \
 -P${PROFILES} -e $1"

# run the command
$CMD

