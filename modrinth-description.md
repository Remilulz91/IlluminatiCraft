# IlluminatiCraft

**IlluminatiCraft adds exactly one item to Minecraft: the Illuminati Pet.**

Right-click it and it hands you a random item — drawn from every crafting recipe loaded
in your game. Vanilla, modded, datapack-added: if something in your instance has a
crafting recipe, the Illuminati can give it to you. Then it goes quiet for two minutes.

This is a Fabric reimplementation of the Illuminati Pet concept from the *Inventory Pets*
mod. It is an independent project, written from scratch, and is not affiliated with or
endorsed by the authors of Inventory Pets.

---

## The ability

**Right-click** — you receive one random craftable item and the pet enters a **120 second
cooldown**. The item name appears above your hotbar. If your inventory is full, the item
drops at your feet.

The draw pool is built from the server's recipe manager, so it adapts to your instance
automatically: install a new mod and its recipes are in the pool the next time the world
loads. No configuration, no compatibility patch, no hardcoded item list. Run
`/illuminati info` to see how many items are currently drawable — around 890 in pure
vanilla 1.21.1, far more in a modpack.

**Illuminati Confirmed** — if the draw lands on the Illuminati Pet itself, you unlock a
challenge advancement worth 100 XP, the server is notified, and a fanfare plays. By
default a 1% forced chance is applied to make this reachable; set `selfDrawBoostPercent`
to `0.0` for a purely random draw, where the odds are roughly 1 in 890 per use.

**Dropping the pet** plays the mod's theme sound. See the *Sounds* section below — that
one ships as a silent placeholder.

---

## Getting the pet

**Crafting** — a pyramid, on any crafting table:

```
 G      G = Gold Ingot
GEG     E = Eye of Ender
ONO     O = Obsidian
        N = Nether Star
```

**Chest loot** — 2% per chest in 13 structure loot tables: dungeons, abandoned
mineshafts, stronghold libraries, desert pyramids, jungle temples, woodland mansions,
pillager outposts, nether fortresses, bastion treasure rooms, ancient cities, buried
treasure, shipwreck treasure and end city treasure. The chance and the whole feature are
configurable. Loot tables are modified through the Fabric API event, never by overwriting
vanilla files, so this stays compatible with datapacks and other mods.

**Creative menu** — Tools & Utilities tab, or `/illuminati give`.

---

## Commands

All require permission level 2 (operator):

| Command | Effect |
|---|---|
| `/illuminati give [players] [count]` | Hand out Illuminati Pets |
| `/illuminati info` | Show the drawable item count and the active configuration |
| `/illuminati reload` | Reload the config and rebuild the recipe cache |
| `/illuminati cooldown reset [players]` | Clear the cooldown |

---

## Configuration

Stored in `config/illuminaticraft.json`, and editable in game with **Mod Menu** +
**Cloth Config**.

You can change the cooldown, allow non-crafting recipes (smelting, smithing…) into the
pool, blacklist specific items or entire namespaces, force the self-draw chance,
broadcast every draw to the server, adjust or disable the sounds, and turn chest loot off
or tune its rate.

---

## Requirements

- **Minecraft 1.21.1**, **Fabric Loader**, **Fabric API** — required
- **Cloth Config** and **Mod Menu** — optional, they only add the in-game config screen
- Required on **both client and server**. The item, its texture and its sounds are
  client-side; the draw, the recipes, the loot and the advancement are server-side.
  Neither side works alone.

---

## Sounds and credits

`illuminati_confirmed` — *Also sprach Zarathustra*, Op. 30 by Richard Strauss (1896,
public domain), in the performance conducted by
[Philip Milman](https://pmmusic.pro/), released under
[CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)
([source](https://www.youtube.com/watch?v=9K3GQdD30F0)). Converted to mono OGG Vorbis for
Minecraft; no other alteration. Full attribution ships inside the JAR as `CREDITS.md`.

`illuminati_theme` — **a silent 3-second placeholder**. The mod deliberately ships no
audio for the drop sound, because no freely licensed track fit the intent. The sound
event is registered and working, so it can be filled in by a resource pack that overrides
`assets/illuminaticraft/sounds/illuminati_theme.ogg`.

Original concept: the Illuminati Pet from *Inventory Pets* by Purplicious_Cow.
Code by [Remilulz_91](https://github.com/Remilulz91), MIT licensed.

---

## Links

- **Source code**: https://github.com/Remilulz91/IlluminatiCraft
- **Bug reports**: https://github.com/Remilulz91/IlluminatiCraft/issues
