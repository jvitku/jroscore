#!/bin/sh

# this spawns child process and creates trap for kill signals.
# if kill signal (ctrl+c) is received, kills child and exits


DEFAULTURI="http://localhost:11311/"

NAME=jroscore
PTH=$BASEDIR"../build/install/jroscore/bin/"
#cd $PTH



java -cp ../../nengoros/nengo/lib-rosjava/*:bin:build ctu.nengoros.Jroscoree &

PID1="$!"

cleanup(){
    kill -s TERM $PID1
}
trap "cleanup" 15 2 18

wait
echo " Sending SIGTERM further.."
