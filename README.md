# IlluminatiCraft

A Fabric port of the **Illuminati Pet** from *Inventory Pets*, for **Minecraft 1.21.1**.

One item, one ability — but it is a good one. Right-click and the Illuminati hands you
**a random craftable item**, drawn from *every* loaded recipe: vanilla **and** every mod
you have installed. And if the Illuminati gives you an Illuminati…
**ILLUMINATI CONFIRMED**.

---

## Features

| Action | Effect |
|---|---|
| **Right-click** | Gives a random item drawn from every loaded crafting recipe (vanilla + modded). **2 minute** cooldown. |
| **Illuminati from an Illuminati** | Unlocks the **Illuminati Confirmed** achievement (+100 XP), announces it server-wide, spawns totem particles and plays the `illuminati_confirmed` sound. |
| **Dropping the item (Q key)** | Plays the `illuminati_theme` sound. |

The item is **fireproof**, does not stack, and is *Epic* rarity.

### How to obtain it

1. **Crafting** (pyramid recipe):

   ```
    G       G = Gold Ingot
   GEG      E = Eye of Ender
   ONO      O = Obsidian
            N = Nether Star
   ```

2. **Structure chests**: 2% per chest — dungeons, mineshafts, stronghold library,
   desert pyramid, jungle temple, woodland mansion, pillager outpost, nether fortress,
   bastion treasure, ancient city, buried treasure, shipwreck treasure, end city
   treasure. Configurable.

3. **Creative menu** (Tools & Utilities tab) and `/illuminati give`.

---

## Commands

All require permission level 2 (OP):

```
/illuminati give [players] [count]   # hand out Illuminati Pets
/illuminati info                     # drawable item count, cooldown, active config
/illuminati reload                   # reload the config and rebuild the recipe cache
/illuminati cooldown reset [players] # clear the cooldown
```

---

## Configuration

File: `config/illuminaticraft.json` — also editable in game through **Mod Menu +
Cloth Config**.

| Option | Default | Description |
|---|---|---|
| `cooldownSeconds` | `120` | Delay between two right-clicks. |
| `craftingRecipesOnly` | `true` | `false` also draws from smelting, smithing, etc. |
| `giveFullRecipeOutput` | `true` | Give the full recipe output (e.g. 4 planks) instead of a single item. |
| `selfDrawBoostPercent` | `1.0` | Forced chance (%) of drawing the Illuminati itself. **`0.0` = fully random**, faithful to Inventory Pets — but the achievement becomes nearly unreachable (~1 in 1500 in pure vanilla). |
| `broadcastDraws` | `false` | Announce every draw to the whole server. |
| `blacklistedItems` / `blacklistedNamespaces` | see file | Items/namespaces excluded from the draw (command blocks, barrier… by default). |
| `enableCustomSounds` | `true` | Enable the mod's sounds. |
| `dropSoundGlobal` | `false` | `true` makes the drop theme audible server-wide. |
| `soundVolume` | `1.0` | Custom sound volume. |
| `addToCreativeTab` | `true` | Add the item to the creative menu (requires a restart). |
| `enableChestLoot` | `true` | Inject the item into chest loot tables. |
| `chestLootChance` | `0.02` | Chance per targeted chest. |

---

## Sounds

Two sounds, two different situations.

### `illuminati_confirmed.ogg` — shipped with the mod

*Also sprach Zarathustra* (Strauss, 1896 — public domain) in the performance conducted by
**Philip Milman**, released under **CC BY 4.0**. That license explicitly allows
redistribution, including inside a published project, as long as credit is given. Full
attribution lives in [CREDITS.md](CREDITS.md), which is bundled inside the JAR.

### `illuminati_theme.ogg` — silent placeholder

The X-Files theme (Mark Snow, 1993) is copyrighted and **no freely licensed version of it
exists**. The repository and the JAR therefore ship a 3-second silence for this slot.
Two ways to fill it:

- **private use**: use the separate resource pack, which overrides the mod's assets
  without ever entering the repository;
- **publishing**: pick a mystery/conspiracy ambient track under **CC0 or CC BY 4.0**
  (Musopen, filmmusic.io, incompetech) and add it to `CREDITS.md`.

> Careful with the wording: "royalty-free" does not mean "public domain". Many
> royalty-free licenses forbid redistributing the audio file as-is — which is exactly what
> a mod or a resource pack does. Only **CC0** and **CC BY** allow it unambiguously.

### Technical requirements

- **OGG Vorbis** only (Minecraft reads neither MP3 nor WAV);
- **mono required** — Minecraft does not spatialize stereo files;
- length: ~10–20 s for the drop theme (it triggers often), up to ~2 min for the
  achievement sound (rare event);
- conversion:
  ```bash
  ffmpeg -i source.mp3 -ac 1 -ar 44100 -c:a libvorbis -q:a 5 illuminati_confirmed.ogg
  ```

---

## Building

```bash
./gradlew build                       # PUBLIC build -> build/libs/illuminaticraft-0.1.1.jar
./gradlew build -PbuildType=debug     # DEBUG build  -> build/libs/illuminaticraft-0.1.1-debug.jar
```

The DEBUG build disables the cooldown and turns the debug flags on — handy for testing the
*Illuminati Confirmed* achievement without waiting two minutes between attempts. It is
**not** attached to releases; grab it from the Actions tab artifacts or build it locally.

Drop the JAR into the `mods/` folder of a **Fabric 1.21.1** instance with **Fabric API**.
Cloth Config and Mod Menu are optional (config screen only).

See [SETUP.md](SETUP.md) for the development environment.

---

## GitHub CI

- `.github/workflows/build.yml` — builds on every push to `main`/`develop`; both JARs are
  uploaded as artifacts (14 day retention).
- `.github/workflows/release.yml` — on a `v*` tag, creates a **GitHub Release** with the
  public JAR attached and `CHANGELOG.md` as the body.

---

## Credits

- Creator and maintainer: **Remilulz_91** — [@Remilulz91](https://github.com/Remilulz91)
- Original concept: the **Illuminati Pet** from *Inventory Pets* (Purplicious_Cow).
- Music: *Also sprach Zarathustra* conducted by [Philip Milman](https://pmmusic.pro/),
  [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) —
  [source](https://www.youtube.com/watch?v=9K3GQdD30F0).
- Code license: MIT. Full attribution details in [CREDITS.md](CREDITS.md).
