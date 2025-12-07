# Mini Projet : Equipe Hybride d’Explorateurs

### Date de réalisation : 07/12/2025
### Année universitaire : 2025-2026
### Établissement : CY Cergy Paris Université
### Membres du groupe : Abdelmadjid Benallegue / Adam Lazib / Kotokpo Josué / Ali Medjkoune

- L'objectif est de simuler une exploration en équipe dans un environnement où des explorateurs recherchent des trésors.
- Ce Mini projet est réalisé dans le cadre d'un atelier SMA (M2-IISC) encadré par M. Tianxio LIU (Tianxiao.Liu@cyu.fr).
------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Règles des explorateurs:



------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Exemple de configuration :
- Dans la classe GameConfig.java, nous pouvons configurer les paramètres principaux du jeu, tels que les dimensions de la fenêtre, la taille des blocs, la vitesse de la simulation, et les comportements des agents. Voici une description détaillée des constantes :

```java
    //définissent les dimensions de la fenêtre de jeu
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;
    //représente la taille d'un bloc dans la grille
    public static final int BLOCK_SIZE = 50;

    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 1000;

    public static final int MAX_BOMB_COUNT = 3;

    public static final int BOMB_EXPLOSION_DELAY = 3;
    public static final int NbRounds = 5;

    // Constantes pour gérer le nombre d'explorateurs en tout genre respectivements communicants, réactifs, et cognitifs)
    public static final int NB_COMMUNICANTS = 5;

    public static final int NB_REACTIFS = 5;

    public static final int NB_COGNITIFS = 5;

```

------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Interface graphique

Pour accéder à l'interface graphique, exécutez le code de la classe TestGUI.java dans un IDE tel qu'Eclipse ou IntelliJ IDEA. Une fois l'interface ouverte, vous verrez deux sections principales :

    Simulation :
        Les explorateurs se déplacent dans l'environnement à la recherche des trésors.
        Les interactions avec les obstacles et les animaux sauvages sont simulées en temps réel.

    Statistiques :
        Affiche des données clés telles que :
            Le nombre d'itérations effectuées.
            Le nombre de trésors collectés.
            Le nombre de combats engagés avec des animaux sauvages.
