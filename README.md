# IlluminatiCraft

Portage Fabric de l'**Illuminati Pet** du mod *Inventory Pets*, pour **Minecraft 1.21.1**.

Un seul item, une seule ability, mais elle est bonne : un clic droit et l'Illuminati te
donne **un item craftable au hasard**, pioché parmi *toutes* les recettes chargées —
vanilla **et** tous les mods installés. Et si l'Illuminati te donne un Illuminati…
**ILLUMINATI CONFIRMED**.

---

## Fonctionnalités

| Action | Effet |
|---|---|
| **Clic droit** | Donne un item aléatoire tiré parmi toutes les recettes de craft chargées (vanilla + modées). Cooldown de **2 minutes**. |
| **Illuminati depuis un Illuminati** | Débloque le succès **Illuminati Confirmed** (+100 XP), annonce serveur, particules totem, et joue le son `illuminati_confirmed`. |
| **Drop de l'item (touche Q)** | Joue le son `illuminati_theme`. |

L'item est **fireproof**, ne s'empile pas, et est de rareté *Epic*.

### Comment l'obtenir

1. **Craft** (recette en pyramide) :

   ```
    G       G = Lingot d'or
   GEG      E = Œil de l'Ender
   ONO      O = Obsidienne
            N = Nether Star
   ```

2. **Coffres de structures** : 2 % par coffre (donjons, mineshafts, bibliothèque de
   forteresse, pyramide, temple de la jungle, manoir, avant-poste, forteresse du Nether,
   bastion, cité antique, trésor enfoui, épave, cité de l'End). Configurable.

3. **Menu créatif** (onglet Outils & Utilitaires) et `/illuminati give`.

---

## Commandes

Toutes en niveau de permission 2 (OP) :

```
/illuminati give [joueurs] [nombre]   # donne des Illuminati Pet
/illuminati info                      # nb d'items piochables, cooldown, config active
/illuminati reload                    # recharge la config + reconstruit le cache de recettes
/illuminati cooldown reset [joueurs]  # remet le cooldown à zéro
```

---

## Configuration

Fichier : `config/illuminaticraft.json` — également éditable en jeu via **Mod Menu +
Cloth Config**.

| Option | Défaut | Description |
|---|---|---|
| `cooldownSeconds` | `120` | Délai entre deux clics droits. |
| `craftingRecipesOnly` | `true` | `false` = les recettes de fourneau, forge, etc. entrent aussi dans le tirage. |
| `giveFullRecipeOutput` | `true` | Donne la quantité complète de la recette (ex. 4 planches) plutôt qu'un seul item. |
| `selfDrawBoostPercent` | `1.0` | Chance forcée (%) de tirer l'Illuminati lui-même. **`0.0` = 100 % aléatoire**, fidèle à Inventory Pets — mais le succès devient quasi inatteignable (~1/1500 en vanilla pur). |
| `broadcastDraws` | `false` | Annonce chaque tirage à tout le serveur. |
| `blacklistedItems` / `blacklistedNamespaces` | cf. fichier | Items/namespaces exclus du tirage (command blocks, barrier… par défaut). |
| `enableCustomSounds` | `true` | Active les sons du mod. |
| `dropSoundGlobal` | `false` | `true` = le thème au drop est entendu par tout le serveur. |
| `soundVolume` | `1.0` | Volume des sons custom. |
| `addToCreativeTab` | `true` | Ajoute l'item au menu créatif (redémarrage requis). |
| `enableChestLoot` | `true` | Injection dans les loot tables de coffres. |
| `chestLootChance` | `0.02` | Chance par coffre concerné. |

---

## Sons — à lire avant de publier

Le dépôt contient **deux fichiers `.ogg` silencieux de 3 secondes**, uniquement là pour
que la structure soit valide :

```
src/main/resources/assets/illuminaticraft/sounds/illuminati_theme.ogg
src/main/resources/assets/illuminaticraft/sounds/illuminati_confirmed.ogg
```

Remplace-les par tes propres fichiers, **en gardant exactement ces noms** :

- `illuminati_theme.ogg` → joué au drop de l'item (ton thème X-Files)
- `illuminati_confirmed.ogg` → joué sur l'auto-invocation (*Also sprach Zarathustra*)

Contraintes techniques :

- format **OGG Vorbis** obligatoire (Minecraft ne lit ni le MP3 ni le WAV) ;
- **mono** fortement recommandé — un fichier stéréo n'est pas spatialisé par Minecraft ;
- conversion : `ffmpeg -i source.mp3 -ac 1 -c:a libvorbis -q:a 5 illuminati_theme.ogg`.

> ⚠️ **Droits d'auteur.** Le thème de *X-Files* (Mark Snow) est une œuvre protégée. La
> composition d'*Also sprach Zarathustra* (Strauss, 1896) est dans le domaine public,
> mais **pas les enregistrements** modernes qui en sont faits. Pour un usage privé entre
> potes, ça ne pose pas de problème pratique. En revanche, si tu publies le `.jar` sur
> GitHub, Modrinth ou CurseForge, les fichiers audio partent avec — c'est un motif
> classique de takedown. Deux options propres : garder les `.ogg` silencieux dans le
> dépôt public et distribuer un pack de sons à part, ou utiliser un enregistrement
> libre de droits (les versions domaine public de Zarathustra existent sur
> Musopen / archive.org).

---

## Build

```bash
./gradlew build                       # build PUBLIC  -> build/libs/illuminaticraft-0.1.0.jar
./gradlew build -PbuildType=debug     # build DEBUG   -> build/libs/illuminaticraft-0.1.0-debug.jar
```

Le build DEBUG désactive le cooldown et active les flags de debug — pratique pour
tester le succès *Illuminati Confirmed* sans attendre 2 minutes entre chaque essai.

Le `.jar` se place dans le dossier `mods/` d'une instance **Fabric 1.21.1** avec
**Fabric API**. Cloth Config et Mod Menu sont optionnels (écran de config uniquement).

Voir [SETUP.md](SETUP.md) pour l'installation de l'environnement de dev.

---

## CI GitHub

- `.github/workflows/build.yml` — build à chaque push sur `main`/`develop`, les deux
  `.jar` sont uploadés en artefacts (14 jours de rétention).
- `.github/workflows/release.yml` — sur un tag `v*`, crée une **Release GitHub** avec
  les deux `.jar` attachés et le contenu de `CHANGELOG.md` en description.

---

## Crédits

- Concept original : **Illuminati Pet** du mod *Inventory Pets* (Purplicious_Cow).
- Implémentation Fabric : Remilulz_91.
- Licence : MIT (le code ; les éventuels fichiers audio que tu ajoutes restent soumis à
  leurs propres droits).
