PLACEHOLDERS — À REMPLACER
==========================

Les deux fichiers .ogg de ce dossier sont des silences de 3 secondes.
Ils existent uniquement pour que la structure du mod soit valide.

  illuminati_theme.ogg      -> joué quand un joueur DROP un Illuminati Pet
                               (ton thème X-Files)

  illuminati_confirmed.ogg  -> joué quand l'Illuminati se donne lui-même
                               (Also sprach Zarathustra — "Illuminati confirmed")

Contraintes :
  - format OGG Vorbis obligatoire (Minecraft ne lit ni MP3 ni WAV)
  - mono recommandé (un fichier stéréo n'est pas spatialisé par Minecraft)
  - garder EXACTEMENT ces deux noms de fichiers

Conversion depuis un MP3 :
  ffmpeg -i source.mp3 -ac 1 -c:a libvorbis -q:a 5 illuminati_theme.ogg

Droits d'auteur : voir la section "Sons" du README.md à la racine du projet
avant toute publication publique du .jar.
