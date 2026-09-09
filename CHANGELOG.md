# Changelog

## 0.1.0 — first release

### Added

- **Illuminati Pet**: a single item — non-stackable, fireproof, Epic rarity.
- **Right-click ability**: gives a random item drawn from every loaded recipe
  (vanilla **and** modded). 120 s cooldown, configurable.
  - The recipe cache is rebuilt on server start and after any `/reload` that changes
    the number of loaded recipes, so newly installed mods are picked up automatically.
  - Only *crafting* recipes are considered by default.
  - Default blacklist: command blocks, structure block, jigsaw, barrier, light,
    debug stick.
- **"Illuminati Confirmed" achievement** (challenge frame, +100 XP), unlocked when the
  Illuminati gives itself, with a server-wide announcement, totem/end rod particles and
  a dedicated sound.
- **Sound on drop**, played locally or server-wide depending on the config.
- **Obtaining**: pyramid crafting recipe, creative menu, `/illuminati give`, and
  injection into 13 structure chest loot tables (2% by default).
- **Commands** `/illuminati give | info | reload | cooldown reset`.
- **Configuration** as JSON plus a Mod Menu / Cloth Config screen.
- Separate **public** and **debug** builds, GitHub Actions CI (build + release on tag).

### Notes

- `illuminati_confirmed.ogg`: *Also sprach Zarathustra*, conducted by Philip Milman,
  CC BY 4.0 — attribution in `CREDITS.md`, shipped inside the JAR.
- `illuminati_theme.ogg`: **silent placeholder**. No freely licensed version of the
  X-Files theme exists. See the "Sounds" section of the README.
- Releases ship the public JAR only. The debug JAR is available as a workflow artifact
  in the Actions tab, or by building it locally.
