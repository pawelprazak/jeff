#!/usr/bin/env bash

LOG_PATH=jeff.log

JAR_PATH="target/jeff-agent-0.1.1-SNAPSHOT-one-jar.jar"

if [ ! -f ${JAR_PATH}  ]; then
    echo "File ${JAR_PATH} not found"
    exit 1
fi

java -showversion \
    -XX:+UseCompressedOops \
    -XX:+TieredCompilation -XX:TieredStopAtLevel=1 \
    -Done-jar.info=true \
    -javaagent:${JAR_PATH}

#    -Done-jar.show.properties=true\
#    -Done-jar.verbose=true \
#    -Done-jar.info=true \
#    -verbose:jni \
#    -javaagent:${JAR_PATH} > ${LOG_PATH}
