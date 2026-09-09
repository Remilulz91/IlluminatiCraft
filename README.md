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

## Sons

Deux sons, deux statuts différents.

### `illuminati_confirmed.ogg` — distribué avec le mod ✅

*Also sprach Zarathustra* (Strauss, 1896 — domaine public), dans l'interprétation dirigée
par **Philip Milman**, publiée sous **CC BY 4.0**. Cette licence autorise explicitement la
redistribution, y compris dans un projet publié, à condition de créditer. L'attribution
complète est dans [CREDITS.md](CREDITS.md), embarqué dans le `.jar`.

### `illuminati_theme.ogg` — placeholder silencieux ⚠️

Le thème de *X-Files* (Mark Snow, 1993) est protégé et **il n'en existe aucune version
sous licence libre**. Le dépôt et le `.jar` ne contiennent donc qu'un silence de 3 s
pour ce slot. Deux façons de le remplir :

- **usage privé** : passer par le resource pack séparé (`IlluminatiCraft-Soundpack`),
  qui écrase les assets du mod sans jamais entrer dans le dépôt ;
- **publication** : utiliser un morceau d'ambiance mystère/conspiration sous **CC0 ou
  CC BY 4.0** (Musopen, filmmusic.io, incompetech), et l'ajouter à `CREDITS.md`.

> Attention au vocabulaire : « royalty-free » ne veut pas dire « domaine public ». Beaucoup
> de licences royalty-free interdisent la redistribution du fichier audio tel quel — ce qui
> est exactement ce que fait un mod ou un resource pack. Seules **CC0** et **CC BY**
> l'autorisent sans ambiguïté.

### Contraintes techniques

- format **OGG Vorbis** obligatoire (Minecraft ne lit ni le MP3 ni le WAV) ;
- **mono obligatoire** — Minecraft ne spatialise pas le stéréo ;
- durées : ~10–20 s pour le thème au drop (il se déclenche souvent), jusqu'à ~2 min pour
  le son du succès (événement rare) ;
- conversion :
  ```bash
  ffmpeg -i source.mp3 -ac 1 -ar 44100 -c:a libvorbis -q:a 5 illuminati_confirmed.ogg
  ```

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
- Musique : *Also sprach Zarathustra* dirigée par [Philip Milman](https://pmmusic.pro/),
  [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) —
  [source](https://www.youtube.com/watch?v=9K3GQdD30F0).
- Licence du code : MIT. Détail complet des attributions dans [CREDITS.md](CREDITS.md).
