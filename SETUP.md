# IlluminatiCraft — Setup Guide

## 1. Prerequisites

- **JDK 21**: https://adoptium.net/temurin/releases/?version=21
- **IntelliJ IDEA Community**: https://www.jetbrains.com/idea/download/
- **Git**: https://git-scm.com/downloads

## 2. Open the project

1. IntelliJ → `File → Open...`
2. Select the `IlluminatiCraft` folder (not a subfolder)
3. IntelliJ detects a Gradle project → **Trust Project**
4. Wait 5–10 minutes: Gradle downloads Minecraft, Fabric Loom, the Yarn mappings and
   the dependencies

## 3. Java version

`File → Project Structure → SDKs` → add your JDK 21
`File → Project Structure → Project → SDK` → select Java 21

## 4. Gradle wrapper

`gradle/wrapper/gradle-wrapper.jar` is committed to the repository, so the wrapper works
out of the box and pins **Gradle 8.10**. Do not delete it — see the troubleshooting
section below for why.

If you ever need to regenerate it:

```bash
gradle wrapper --gradle-version 8.10
```

## 5. Run Minecraft in dev mode

Once the project is imported, IntelliJ generates the run configurations:

- **runClient**: Minecraft 1.21.1 with the mod
- **runServer**: a dedicated 1.21.1 server with the mod

## 6. Test the mod

```
/give @s illuminaticraft:illuminati_pet
```
or
```
/illuminati give
```

Then right-click. To test the achievement without waiting:

```bash
./gradlew runClient -PbuildType=debug
```

or set `selfDrawBoostPercent` to `100.0` in the config, which forces the self-draw on
every click.

Check how many items can be drawn (useful to confirm mods are being picked up):

```
/illuminati info
```

## 7. Building the JAR

```bash
./gradlew build                       # PUBLIC
./gradlew build -PbuildType=debug     # DEBUG
```

Output goes to `build/libs/`. Releases only ship the public JAR; the debug JAR stays in
the Actions artifacts or on your machine.

---

## Troubleshooting

### "Cannot resolve symbol Item / PlayerEntity / ..."
Gradle has not finished downloading the Yarn mappings. Wait, or run `./gradlew genSources`.

### CI: `Failed to apply plugin 'fabric-loom'` / `Problems.forNamespace`

Symptom in GitHub Actions:

```
./gradlew: Permission denied            (or: Could not find or load main class GradleWrapperMain)
Welcome to Gradle 9.7.1!
> Failed to apply plugin 'fabric-loom'.
   > 'ProblemReporter Problems.forNamespace(String)'
```

Chain of events: `./gradlew` fails (missing executable bit, or missing
`gradle-wrapper.jar`) → the job falls back to the runner's own Gradle (9.x) →
**Fabric Loom 1.7 is not compatible with Gradle 9** (`Problems.forNamespace` was removed).

Fixed by committing `gradle/wrapper/gradle-wrapper.jar` (which pins Gradle 8.10) and
keeping the executable bit on `gradlew`:

```bash
git update-index --chmod=+x gradlew
```

The workflows also run `chmod +x ./gradlew` and verify the wrapper JAR before any Gradle
invocation, and deliberately do **not** use `gradle/actions/setup-gradle`, which falls
back to Gradle 9 whenever the wrapper is unusable.

### `incompatible parameter types in lambda` in `ModLootTables.java`

The signature of `LootTableEvents.MODIFY` depends on the Fabric API version. The code uses
the 4-parameter form (loot API **v3**, Fabric API 0.102.1+1.21.1):

```java
LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
```

If your version expects 3 parameters, just drop `registries`:

```java
LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
```

### `incompatible types: Reference<SoundEvent>`

Some Yarn 1.21.1 sound fields are `RegistryEntry.Reference<SoundEvent>` and need
`.value()`. The mod already avoids this by resolving vanilla sounds through the registry
(`SoundUtil.playVanillaTo`).

### The mixin does not load

Check that `illuminaticraft.mixins.json` is listed in `fabric.mod.json` (`"mixins"`
section) and that the class sits in the `fr.illuminaticraft.mixin` package.

### The sound does not play

- The file must be real **OGG Vorbis**, not a renamed MP3.
- It must be named exactly `illuminati_theme.ogg` / `illuminati_confirmed.ogg`.
- The **Jukebox/Records** volume slider must be above 0 — the mod's sounds use the
  `record` category.
