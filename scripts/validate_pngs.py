#!/usr/bin/env python3
"""Fail CI when a packaged PNG is structurally invalid.

Minecraft 1.21.1 loads GUI textures through NativeImage/STB. A malformed PNG can
still be accepted by permissive image editors while Minecraft replaces it with
the magenta/black missing-texture pattern. This validator catches the class of
failure seen in alpha.12 before a JAR is published.
"""

from __future__ import annotations

import binascii
import struct
import sys
import zlib
from pathlib import Path

PNG_SIGNATURE = b"\x89PNG\r\n\x1a\n"
TEXTURE_ROOT = Path("src/main/resources/assets")
REQUIRED_FIRST = b"IHDR"
REQUIRED_LAST = b"IEND"


def validate_png(path: Path) -> list[str]:
    data = path.read_bytes()
    errors: list[str] = []

    if not data.startswith(PNG_SIGNATURE):
        return ["invalid PNG signature"]

    offset = len(PNG_SIGNATURE)
    chunk_index = 0
    seen_ihdr = False
    seen_iend = False
    idat = bytearray()

    while offset < len(data):
        if offset + 8 > len(data):
            errors.append(f"truncated chunk header at byte {offset}")
            break

        length = struct.unpack(">I", data[offset : offset + 4])[0]
        chunk_type = data[offset + 4 : offset + 8]
        offset += 8

        if any(not (65 <= byte <= 90 or 97 <= byte <= 122) for byte in chunk_type):
            errors.append(
                f"invalid/unknown PNG chunk type {chunk_type!r} at chunk {chunk_index}"
            )
            break

        if offset + length + 4 > len(data):
            errors.append(
                f"truncated {chunk_type.decode('ascii')} chunk: "
                f"declares {length} data bytes with only {len(data) - offset} bytes remaining"
            )
            break

        payload = data[offset : offset + length]
        stored_crc = struct.unpack(">I", data[offset + length : offset + length + 4])[0]
        calculated_crc = binascii.crc32(chunk_type)
        calculated_crc = binascii.crc32(payload, calculated_crc) & 0xFFFFFFFF
        if stored_crc != calculated_crc:
            errors.append(
                f"CRC mismatch in {chunk_type.decode('ascii')} chunk {chunk_index}"
            )

        if chunk_index == 0 and chunk_type != REQUIRED_FIRST:
            errors.append("IHDR is not the first chunk")

        if chunk_type == b"IHDR":
            if seen_ihdr:
                errors.append("multiple IHDR chunks")
            seen_ihdr = True
            if length != 13:
                errors.append(f"IHDR length is {length}, expected 13")

        if chunk_type == b"IDAT":
            idat.extend(payload)

        offset += length + 4
        chunk_index += 1

        if chunk_type == REQUIRED_LAST:
            seen_iend = True
            if length != 0:
                errors.append("IEND chunk must be empty")
            if offset != len(data):
                errors.append(f"{len(data) - offset} trailing byte(s) after IEND")
            break

    if not seen_ihdr:
        errors.append("missing IHDR")
    if not seen_iend:
        errors.append("missing IEND")
    if not idat:
        errors.append("missing IDAT image data")
    else:
        try:
            decompressor = zlib.decompressobj()
            decompressor.decompress(bytes(idat))
            decompressor.flush()
            if not decompressor.eof:
                errors.append("IDAT zlib stream is incomplete")
        except zlib.error as exc:
            errors.append(f"invalid IDAT zlib stream: {exc}")

    return errors


def main() -> int:
    pngs = sorted(TEXTURE_ROOT.rglob("*.png"))
    if not pngs:
        print("No PNG textures found.", file=sys.stderr)
        return 1

    failed = False
    for path in pngs:
        errors = validate_png(path)
        if errors:
            failed = True
            print(f"[INVALID] {path}")
            for error in errors:
                print(f"  - {error}")
        else:
            print(f"[OK] {path}")

    if failed:
        print("\nPNG validation failed. Refusing to publish malformed Minecraft textures.", file=sys.stderr)
        return 1

    print(f"\nValidated {len(pngs)} PNG texture(s).")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
