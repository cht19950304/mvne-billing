#!/bin/bash


echo ${MCB_HOME}
echo ${MCB_APPID}


function run_cmd()
{

v_java_opts="-XX:+HeapDumpOnOutOfMemoryError"
v_java_opts="${v_java_opts} -XX:+HeapDumpOnOutOfMemoryError"
v_java_opts="${v_java_opts} -XX:HeapDumpPath=${MCB_HOME}/${MCB_APPID}/log/mvne-info-manage.err.dump"

cd ${MCB_HOME}/${MCB_APPID}/main
pwd
v_cmd="java ${v_java_opts} -jar mvne-info-manage-0.0.1-SNAPSHOT.jar"

echo ${v_cmd}
${v_cmd}
}

run_cmd
