# Changelog

## 0.4.0

### Fixed

- **The cooldown survived neither a relogin nor a world reload.** Minecraft's
  `ItemCooldownManager` lives in memory on the player entity and is discarded on
  disconnect, so leaving and rejoining reset it — the pet could be spammed at will. The
  expiry is now stored per player in `illuminaticraft-cooldowns.json` inside the world
  folder, as a world tick, and re-applied on join. World time only advances while the
  world is running, so pausing a singleplayer game does not burn the cooldown either.
- **No feedback at all when clicking during the cooldown.** Minecraft filters the use of
  an item on cooldown client-side, before anything reaches the server, so the server-side
  message and sound added in 0.3.0 could never fire. The refusal is now handled on the
  client, where the click actually happens.

### Changed

- The item is now simply named **Illuminati** instead of "Illuminati Pet". The registry
  id is unchanged, so existing worlds and items keep working.
- Action bar messages stay up for **5 seconds** instead of vanilla's 3, by re-sending
  them before they fade.
- The remaining cooldown is displayed as **minutes and seconds** (`1:35`) rather than a
  raw number of seconds.

## 0.3.0

### Added

- **Ready notification.** When the cooldown expires, a message appears above the hotbar
  with a short chime, the way Inventory Pets announced the pet was usable again. Watched
  players are only tracked while a cooldown is running, and checked twice a second.
  Toggle with `notifyWhenReady`.
- **Feedback sound when the pet is still recharging**, alongside the existing countdown
  message.

### Changed

- Draw message now follows the original format: **`<item> Confirmed!!!`**, shown above
  the hotbar. A separate line is used when the recipe yields more than one item.
- The cooldown is now checked on the client as well as the server. The hand no longer
  swings when the pet is refused, instead of swinging and being rejected afterwards.

## 0.2.0

### Changed

- **The theme now plays on the draw, not on the drop.** `illuminati_theme` fires on every
  successful right-click, the way the original Inventory Pets item behaves. Dropping the
  pet no longer plays anything.
- The short vanilla beacon chime that used to accompany a draw is gone — the theme
  replaces it.
- Config: `dropSoundGlobal` is renamed **`themeSoundGlobal`** and now controls whether the
  draw theme is heard by the whole server or only by the player using the pet. Existing
  config files fall back to the default (off) for the new key.

### Removed

- The `PlayerEntity.dropItem` mixin, which only existed to play the drop sound. The mod no
  longer ships any mixin, which removes its most version-fragile piece.

## 0.1.3

### Changed

- The in-game "Website" button now points to the Modrinth page instead of the GitHub
  repository. Source and issue links are unchanged.
- Automated Modrinth publishing: tagging a release also uploads the JAR to Modrinth,
  using only that version's section of the changelog as release notes.

## 0.1.2

### Fixed

- The mod declared `"minecraft": "~1.21.1"`, which Fabric reads as *any 1.21.x from
  1.21.1 onwards*. It would therefore load on 1.21.2 through 1.21.11 and crash: several
  APIs it relies on changed in 1.21.2 (`ItemCooldownManager` now takes an `ItemStack`,
  `TypedActionResult` was renamed, loot conditions and recipe JSON changed shape). The
  dependency is now pinned to **1.21.1 exactly**, so Fabric Loader refuses to start it on
  an unsupported version instead of failing at runtime.

## 0.1.1

### Fixed

- `/illuminati` command feedback was hardcoded in French and ignored the client
  language. All command output now goes through translation keys and is available in
  English and French.

### Verified in game

First runtime pass on Minecraft 1.21.1: recipe cache (891 drawable items in pure
vanilla), right-click draw, "Illuminati Confirmed" achievement with its sound, crafting
recipe, and chest loot injection all confirmed working.

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
