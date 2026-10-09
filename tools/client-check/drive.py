# Minimal X input / screenshot driver for checking the real client on a virtual display (tools/client-check).
import sys, time, subprocess, os
from Xlib import X, XK, display
from Xlib.ext import xtest
d = display.Display(os.environ["DISPLAY"])
def move(x, y): xtest.fake_input(d, X.MotionNotify, x=x, y=y); d.sync()
def click(x, y, button=1):
    move(x, y); time.sleep(0.15)
    xtest.fake_input(d, X.ButtonPress, button); d.sync(); time.sleep(0.08)
    xtest.fake_input(d, X.ButtonRelease, button); d.sync(); time.sleep(0.3)
def keycode(name):
    ks = XK.string_to_keysym(name)
    return d.keysym_to_keycode(ks)
def key(name, hold=0.08):
    kc = keycode(name)
    xtest.fake_input(d, X.KeyPress, kc); d.sync(); time.sleep(hold)
    xtest.fake_input(d, X.KeyRelease, kc); d.sync(); time.sleep(0.2)
def down(name): xtest.fake_input(d, X.KeyPress, keycode(name)); d.sync()
def up(name): xtest.fake_input(d, X.KeyRelease, keycode(name)); d.sync()
def shot(path):
    subprocess.run(["import", "-window", "root", path], check=True)
# commands: click x y | rclick x y | move x y | key name [hold] | down name | up name | sleep s | shot path
for cmd in sys.argv[1:]:
    p = cmd.split()
    if p[0] == "click": click(int(p[1]), int(p[2]))
    elif p[0] == "rclick": click(int(p[1]), int(p[2]), 3)
    elif p[0] == "move": move(int(p[1]), int(p[2]))
    elif p[0] == "key": key(p[1], float(p[2]) if len(p) > 2 else 0.08)
    elif p[0] == "down": down(p[1])
    elif p[0] == "up": up(p[1])
    elif p[0] == "sleep": time.sleep(float(p[1]))
    elif p[0] == "shot": shot(p[1])
    elif p[0] == "type":
        text = cmd[5:]
        names = {" ": "space", "/": "slash", ":": "colon", "_": "underscore", ".": "period", "-": "minus"}
        for ch in text:
            if ch == "_" or ch == ":" or ch.isupper():
                down("Shift_L"); key(names.get(ch, ch.lower()) if ch not in "_:" else ("minus" if ch == "_" else "semicolon"), 0.03); up("Shift_L")
            else: key(names.get(ch, ch), 0.03)
