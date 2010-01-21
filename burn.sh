#!/bin/bash
# This is a Ubuntu Linux script to quickly burn the apk onto the openmoko phone
# Be sure to have $PATH set to SDK/tools, and $ADBHOST set to 192.168.0.202.
lsusb
adb kill-server
sudo ifconfig eth1 192.168.0.200
echo "*** Press Ctrl+C to continue if lsusb and ping are successful ***"
echo "*** Press Ctrl+Z to exit ***"
ping 192.168.0.202
adb devices
adb install -r bin/iRoll.apk
adb kill-server
