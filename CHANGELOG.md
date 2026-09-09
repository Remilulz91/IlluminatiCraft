# Changelog

## 0.1.0 — première version

### Ajouté

- **Illuminati Pet** : item unique, non empilable, fireproof, rareté Epic.
- **Ability clic droit** : donne un item aléatoire pioché parmi toutes les recettes
  chargées (vanilla + mods), cooldown de 120 s configurable.
  - Le cache de recettes se reconstruit au démarrage du serveur et après tout `/reload`
    modifiant le nombre de recettes — les mods ajoutés sont donc pris en compte
    automatiquement.
  - Filtre par défaut sur les recettes de type *crafting* uniquement.
  - Blacklist par défaut : command blocks, structure block, jigsaw, barrier, light,
    debug stick.
- **Succès « Illuminati Confirmed »** (challenge, +100 XP) débloqué quand l'Illuminati
  se donne lui-même, avec annonce serveur, particules totem/end rod et son dédié.
- **Son au drop** de l'item (local ou serveur entier selon la config).
- **Obtention** : recette de craft en pyramide, menu créatif, `/illuminati give`, et
  injection dans 13 loot tables de coffres de structures (2 % par défaut).
- **Commandes** `/illuminati give | info | reload | cooldown reset`.
- **Config** JSON + écran Mod Menu / Cloth Config.
- Builds **public** et **debug** séparés, CI GitHub Actions (build + release sur tag).

### Notes

- `illuminati_confirmed.ogg` : *Also sprach Zarathustra* dirigé par Philip Milman,
  CC BY 4.0 — attribution dans `CREDITS.md`, embarqué dans le JAR.
- `illuminati_theme.ogg` : **placeholder silencieux**, aucune version sous licence libre
  du thème X-Files n'existe. Voir la section « Sons » du README.
- CI : Fabric Loom 1.7 est incompatible Gradle 9, les workflows forcent Gradle 8.10 via
  le wrapper.
