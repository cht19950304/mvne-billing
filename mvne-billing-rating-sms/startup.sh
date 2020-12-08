#!/bin/bash


echo ${MCB_HOME}
echo ${MCB_APPID}


function run_cmd()
{

v_java_opts="-XX:+HeapDumpOnOutOfMemoryError"
v_java_opts="${v_java_opts} -XX:+HeapDumpOnOutOfMemoryError"
v_java_opts="${v_java_opts} -XX:HeapDumpPath=${MCB_HOME}/${MCB_APPID}/log/mvne-billing-rating-sms.err.dump"

cd ${MCB_HOME}/${MCB_APPID}
pwd
v_cmd="java ${v_java_opts} -jar mvne-billing-rating-sms.jar"

echo ${v_cmd}
${v_cmd}
}

run_cmd
