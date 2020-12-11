# Test technique JCDecaux
Ce projet est un test technique pour JCDecaux. C'est une application Android développée en Kotlin.
Elle a pour objectif d'afficher sur une carte les stations de vélos en libre-service provenant d'un api protégé.  

## Installation

Pour tester le projet, il est necessaire d'ajouter les clés d'API de Google et JCDeacaux.
Ces dernières ont été cachées à des fins de sécurité.
### Google API
Mettre la clé d'API Google fournie dans le fichier app/src/debug/res/values/google_maps_api.xml
### JCDecaux API
Mettre la clé d'API JCDecaux fournie dans le fichier gradle.properties

# Description de l'application
## Analyse fonctionnelle

### Comportement de l'application
L'application est composée de 2 écrans:
- Une carte affchant les stations autour de l'utilisateur
- Un écran de détails de la station lorsque l'on clique sur une station

Au lancement l'application demande la permission de l'utilisateur pour le localiser et afficher les stations autour de lui.
S'il refuse, nous affichons par défaut la France entière.

### Comportement du code
Pour faire cela, l'application télécharge les données via l'API fourni par l'entreprise.
Nous récupérons seulement les informations dont nous avons besoin à des fins d'optimisation. 
Ces informations sont directement envoyées à la vue (pas de stockage en base de données interne).

## Analyse technique
### Architecture
La structure de l'application est en MVP (Model View Presenter)
Le "Presenter" assure l'échange entre la vue et le modèle.
Nous effectuons les appels serveurs dans un "repository" afin d'isoler la logique métier dans le Presenter.

### Choix des librairies

#### Récupération des données de l'API
 
Pour faire une requête HTTP vers le l'API de JCDecaux, nous utilisons la librairie **Retrofit** développée par Square.
Cette librairie a également l'avantage de simplifier l'appel vers le serveur. Accompagnée de la librairie **GSON converter**, elle permet également de simplifier la conversion de l'objet JSON reçu en liste d'objets prête à être intégrée dans la base de données. Nous avons également personnalisé le convertisseur afin de seulement récupérer les informations dont nous avons besoin.

#### Affichage de la carte
Pour afficher la carte et ajouter les stations de vélo, nous avons utiliser **Google Maps SDK**. 
Afin de gérer la permission de localisation, nous utilisons **Permissions Dispatcher**.
Nous aurions pu nous contenter de la gestion de Google Maps SDK MyLocation.
Ce fut un choix personnel, notamment par rapport à la gestion plus facile des cas de refus de Permissions Dispatcher.

#### Autres librairies utilisées

**RxJava**: Cette librairie est intervenue en complément de Retrofit afin de réaliser l'appel en asynchrone assez simplement.
**Google Maps MyLocation**: Pour récupérer et afficher la position de l'utilisateur