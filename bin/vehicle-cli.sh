#!/bin/bash

# runs vehicle-cli-*.jar

clear

export LANG=ru_RU.UTF-8
export LANGUAGE=ru
export LC_CTYPE=ru_RU.UTF-8

export TZ=UTC

CURRENT_DIR=$(cd "`dirname $0`" && pwd)

# getting the full name of jar-file
JAR_FILE=vehicle-cli-*.jar
for F in "$CURRENT_DIR"/*; do
    BASE_NAME=$(basename "${F}")
    if [[ $BASE_NAME == $JAR_FILE ]] ; then
        JAR_FILE=${CURRENT_DIR}/${BASE_NAME}
        break
    fi
done

JAVA_VM_OPTS=" -Xmx1024m"
JAVA_VM_OPTS+=" -Xms256m"
JAVA_VM_OPTS+=" -XX:-OmitStackTraceInFastThrow"

## doesn't work because it can't handle spaces in the path:
##   JAVA_CMD="java ${JAVA_VM_OPTS} -jar ""${JAR_FILE}"""
##
## that's why didn't use one of these variants:
##   exec ${JAVA_CMD}                     &&
##   ${JAVA_CMD}                          &&

java ${JAVA_VM_OPTS} -jar "${JAR_FILE}" "$@"
