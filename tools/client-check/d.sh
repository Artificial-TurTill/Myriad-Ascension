#!/bin/bash
# Sends input to / takes screenshots of a client started with xvfb-run (see README.md in this folder).
A=$(ps aux | grep "[X]vfb" | grep -o "\-auth [^ ]*" | head -1 | cut -d' ' -f2)
D=$(ps aux | grep "[X]vfb" | grep -o "Xvfb :[0-9]*" | head -1 | cut -d' ' -f2)
XAUTHORITY=$A DISPLAY=$D python3 -I "$(dirname "$0")/drive.py" "$@"
