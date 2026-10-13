---
date: 2026-10-13
tags: [rambling, myriad-realm, stats, realm-advancement]
status: offen
related: ["IDEA-001 Cultivation Realms", "IDEA-002 Body Tempering"]
---
# 2026-10-13 — Stats, Release und Realm Advancement

> Rambling-Session, noch keine Umsetzung. Entscheidung und Auftrag folgen später.

## Originalnotiz (sinngemäß, vom User)
- Das System wurde gegenüber DBC+ etwas stark abgeändert; es fehlen ein paar stat-bezogene Aspekte, z. B. das
  **prozentuale Stärken der Stats, gemessen an den eigenen Stats, ab einem Release von 5 %**.
- **Bei 0 % Release bleibt einzig der CON-Stat weiterhin im Effekt.**
- Schnellere Mobilität gibt es aktuell nur über Turbo (Qi Surge). Das ist eingeschränkt, aber **nicht unbedingt
  schlecht**: In Myriad Realm ist die Stat-Nutzung am Anfang stark limitiert, für den Start passt das sogar.
- Gesucht ist ein System, mit dem das insgesamt aufgeht:
  - **Body Tempering**: Nutzen beschränkt auf **STR, DEX und CON**, dazu soll **MND** von Anfang an gehören.
  - Die Frage ist, wie man das auf Myriad Realm überträgt, ein **Martial Peak**-inspiriertes Werk.
- Vorbereiten auf ein **Realm-Advancement-System**:
  - **kleine Ränge innerhalb eines Realms** (Minor Advancement),
  - **große Realm-Sprünge**, z. B. **Body Tempering → Initial Element** (Major Advancement).
  - **Initial Element schaltet die Stats WIL und SPI frei.**
- Prozess: Anders als bei DBC+ gibt es kaum bis kein Material zum Portieren; hier wird überwiegend **selbst
  entwickelt**.

## Ist-Stand im Code (0.1.2, zur Einordnung)
| Stat | Statistik | Wirkung bei 0 % Release | Ab Release > 0 % |
| --- | --- | --- | --- |
| STR | Melee | 0 (released) | Melee × Release % auf jeden kräftigen Schlag |
| DEX | Defense | 0 (released) | passiver Anteil (20 %) von Defense × Release % |
| DEX | Surge speed | – (Qi Surge braucht > 0 %) | Laufgeschwindigkeit nur mit Qi Surge |
| CON | Body, Stamina | **voll aktiv** | voll aktiv; hohe Release bremst nur die Regeneration |
| WIL | Qi Power | 0 (released) | nur angezeigt, noch ohne Nutzung (keine Qi-Techniken) |
| MND | Skill-Kapazität, TP | Kapazität aktiv | TP-Gewinn pro Treffer erst **ab 5 %** Release |
| SPI | Max Qi | Pool existiert | Qi wird bisher nur von Qi Surge / Meditation genutzt |

Also: Die prozentuale Release-Skalierung (DBC "released" Stats) ist im Code vorhanden, kann aber erst greifen, wenn
die Release aufgeladen ist (C halten, 5 % pro Sekunde). Ein DBC-Unterschied: Sprinten ist vanilla (DBC lief beim
Sprinten mit DEX schneller); nur Qi Surge läuft schneller.

## Offene Fragen für das Design
1. Bedeutet "ab 5 %" eine **Schwelle** (unter 5 % wirkt nichts, darüber prozentual) oder die bisherige lineare
   Skalierung in 5-%-Schritten?
2. **Stat-Freischaltung pro Realm**: Sind gesperrte Stats (WIL, SPI vor Initial Element) nicht trainierbar, nicht
   wirksam, oder beides? Bleiben sie auf 1?
3. **Body Tempering mit MND**: Welche Rolle spielt MND am Anfang (Skill-Kapazität, Wahrnehmung, TP)?
4. **Minor Advancement** (Stufen 1-9 / Stage-Order-Rank aus dem Alpha-16-Archiv?): Was löst einen Rang aus
   (Attribut-Summen, TP, Training, Prüfung) und was gibt er (Attribut-Limit, Multiplikator, Skills)?
5. **Major Advancement** (Breakthrough Body Tempering → Initial Element): Bedingungen, Risiko / Scheitern,
   was sich qualitativ ändert (neue Stats, Qi-Nutzung, Qi Surge, Formen)?
6. Soll das Realm-System **generisch in der Foundation** liegen (eine "Progression Track"-Mechanik, die jede
   Erweiterung mit eigenen Realms füllt) und **Myriad Realm** nur die Realms als ARSN-Content liefern?

## Mögliche Bausteine (Vorschlag, nicht beschlossen)
- Foundation-Erweiterung "Realm / Progression": geordnete Liste von Major Realms mit Minor Ranks; pro Rang Felder wie
  `unlocked_attributes`, `attribute_limit`, `stat_multiplier`, `requirements` (z. B. Attributsumme, TP, Skill).
- Gesperrte Attribute: im Sheet grau, kein "+" und keine Wirkung, bis der Realm sie freigibt.
- Breakthrough als eigene Aktion (Taste / X-Menü-Feld "Breakthrough"), mit Voraussetzungen und ggf. Risiko.
- Bezug zum Archiv: [[IDEA-001 Cultivation Realms]] (Realm-Hierarchie, Stage / Order / Rank),
  [[IDEA-002 Body Tempering]] (Tempered Body 1-9: Körper → Wahrnehmung → bewusstes Qi-Sammeln).

## Status
Offen. Wartet auf Gedanken und Auftrag des Users.

Zurück zu [[Rambling]]
