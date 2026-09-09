# IlluminatiCraft — Guide d'installation

## 1. Prérequis

- **JDK 21** : https://adoptium.net/temurin/releases/?version=21
- **IntelliJ IDEA Community** : https://www.jetbrains.com/idea/download/
- **Git** : https://git-scm.com/downloads

## 2. Ouvrir le projet

1. IntelliJ → `File → Open...`
2. Sélectionner le dossier `IlluminatiCraft` (pas un sous-dossier)
3. IntelliJ détecte un projet Gradle → **Trust Project**
4. Attendre 5–10 min : Gradle télécharge Minecraft, Fabric Loom, les mappings Yarn et
   les dépendances

## 3. Version de Java

`File → Project Structure → SDKs` → ajouter le JDK 21
`File → Project Structure → Project → SDK` → sélectionner Java 21

## 4. Générer le wrapper Gradle (si manquant)

`gradle/wrapper/gradle-wrapper.jar` n'est pas versionné. Trois options :

**A** — Laisser IntelliJ utiliser son Gradle intégré :
`Settings → Build → Gradle → Use Gradle from: → 'gradle-wrapper.properties' ou Specified location`

**B** — Si Gradle est installé en local :
```bash
gradle wrapper --gradle-version 8.10
```

**C** — Télécharger le jar officiel :
```bash
curl -sSL -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.10.0/gradle/wrapper/gradle-wrapper.jar
```

La CI GitHub fait automatiquement l'option C.

## 5. Lancer Minecraft en dev

Une fois le projet importé, IntelliJ génère les configurations :

- **runClient** : Minecraft 1.21.1 avec le mod
- **runServer** : serveur dédié 1.21.1 avec le mod

## 6. Tester le mod

```
/give @s illuminaticraft:illuminati_pet
```
ou
```
/illuminati give
```

Puis clic droit. Pour tester le succès sans attendre :

```bash
./gradlew runClient -PbuildType=debug
```
ou, en jeu, mets `selfDrawBoostPercent` à `100.0` dans la config, ce qui force
l'auto-invocation à chaque clic.

Vérifier le nombre d'items piochables (utile pour valider que les mods sont bien pris
en compte) :

```
/illuminati info
```

## 7. Build du .jar

```bash
./gradlew build                       # PUBLIC
./gradlew build -PbuildType=debug     # DEBUG
```

Sortie dans `build/libs/`.

---

## Problèmes fréquents

### "Cannot resolve symbol Item / PlayerEntity / ..."
Gradle n'a pas fini de télécharger les mappings Yarn. Attendre, ou `./gradlew genSources`.

### `incompatible parameter types in lambda` dans `ModLootTables.java`
La signature de `LootTableEvents.MODIFY` dépend de la version de Fabric API. Le code
utilise la version à 4 paramètres (API loot **v3**, Fabric API 0.102.1+1.21.1) :

```java
LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
```

Si ta version attend 3 paramètres, retire simplement `registries` :

```java
LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
```

### `incompatible types: Reference<SoundEvent>`
Certains sons Yarn 1.21.1 sont des `RegistryEntry.Reference<SoundEvent>` et demandent
`.value()`. Le mod contourne déjà le problème en résolvant les sons vanilla par leur id
(`SoundUtil.playVanillaTo`).

### Le mixin ne se charge pas
Vérifier que `illuminaticraft.mixins.json` est bien listé dans `fabric.mod.json`
(section `"mixins"`) et que la classe est dans le package `fr.illuminaticraft.mixin`.

### Le son ne se joue pas
- Le fichier doit être en **OGG Vorbis**, pas en MP3 renommé.
- Il doit s'appeler exactement `illuminati_theme.ogg` / `illuminati_confirmed.ogg`.
- Le volume du canal **Jukebox/Disques** doit être > 0 dans les options audio (les sons
  du mod utilisent la catégorie `record`).

### CI : `Failed to apply plugin 'fabric-loom'` / `Problems.forNamespace`

Symptôme dans GitHub Actions :

```
./gradlew: Permission denied
Welcome to Gradle 9.7.1!
> Failed to apply plugin 'fabric-loom'.
   > 'ProblemReporter Problems.forNamespace(String)'
```

Enchaînement : `gradlew` a été commité sans le bit exécutable (fichier créé sous
Windows) → l'action `gradle/actions/setup-gradle` ne peut pas l'utiliser et
retombe sur **sa propre** version de Gradle (9.x) → **Fabric Loom 1.7 n'est pas
compatible Gradle 9** (la méthode `Problems.forNamespace` a disparu).

Corrigé dans les workflows : `gradle/actions/setup-gradle` a été retiré, le cache
Gradle passe par `actions/setup-java` (`cache: gradle`), et une étape
« Prepare Gradle wrapper » fait `chmod +x ./gradlew` + récupère
`gradle-wrapper.jar` avant toute invocation. Le wrapper impose Gradle 8.10.

Côté dépôt, poser le bit exécutable une bonne fois pour toutes :

```bash
git update-index --chmod=+x gradlew
git commit -m "chmod +x gradlew"
```

Et de préférence commiter `gradle/wrapper/gradle-wrapper.jar` (IntelliJ le génère
à l'import, ou `gradle wrapper --gradle-version 8.10`) pour ne pas dépendre d'un
téléchargement à chaque run.
