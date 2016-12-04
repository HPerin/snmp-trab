#!/usr/bin/env bash
OUTPUT=`ps -eo pid,comm,lstart,etime,time,args | grep $1`
SERVICE_NAME=`echo $OUTPUT | cut -d ' ' -f 2`

if [ "$SERVICE_NAME" == "$1" ]; then
    SERVICE_UPTIME=`echo $OUTPUT | cut -d ' ' -f 8`
    SERVICE_PATH=`echo $OUTPUT | cut -d ' ' -f 10`

    echo "1"
    echo $SERVICE_NAME
    echo $SERVICE_UPTIME
    echo $SERVICE_PATH
else
    echo "0"
fi