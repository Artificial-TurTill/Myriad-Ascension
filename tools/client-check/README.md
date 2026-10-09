# Client check on a virtual display

The build host has no screen, but the real game client runs on Xvfb with Mesa software OpenGL. This lets a coding
session open the mod's screens, click through them and take screenshots.

```sh
pip install python-xlib            # once per container
LIBGL_ALWAYS_SOFTWARE=1 MESA_GL_VERSION_OVERRIDE=4.5 \
  xvfb-run -a -s "-screen 0 1280x720x24" ./gradlew :myriad-realm:runClient &   # both mods
tools/client-check/d.sh "click 638 567" "sleep 2" "shot /tmp/shot.png"         # input + screenshot
```

Commands (one argument each): `click x y`, `rclick x y`, `move x y`, `key name [seconds]` (X keysym names: `v`,
`Return`, `Escape`, `F5`), `down name` / `up name` (hold a key), `type text` (chat commands), `sleep seconds`,
`shot path`. Coordinates are screen pixels of the 1280 x 720 display (the game window is 854 x 480 at 213, 120).

Notes: no sound device (the sound engine is switched off); Mojang servers are unreachable (offline "Dev" player);
screens do not pause the game, so test in a peaceful world. Stop the client with `kill $(pgrep -x java)` and
`kill $(pgrep -x Xvfb)`, never `pkill -f runClient` from the same shell.
