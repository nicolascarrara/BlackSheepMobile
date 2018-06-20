# BlackSheepMobile

 
 
  API et technologies utilisée 
  =================================================================
  
  L'API utilisé pour la récupération d'informations concernant les Films est celle de   __www.themoviedb.org__ .
  Il est également utilisé la  __Librairie YoutubePlayer__ afin de pouvoir intégrer les bandes annonces dans les détails des films.
  La base de données locale pour la gestion des films marqué comme vu ou coup de coeur est une base de données  __SQLite__ .
  
  Plateforme utilisé pour le développement
  ==================================================================
  
  Le développement a été effectué avec Android Studio sur un Macbook Pro OSX High Sierra (10.13) et ne possédant pas de smartphone Androïd, les tests ont été effectués sur un émulateur intégré a Android Studio ( Nexus 5X ).
  
  Utilisation de l'application 
  ==================================================================
  
L'application est composée en plusieurs partie :
- La partie `Découverte` sur laquelle s'ouvre l'application,
elle propose une liste de films récent / ayant du succès, 
il est possible de scroller vers le bas, de nouveaux films se chargeant ainsi a la volée pour ne pas surcharger l'application.

- En haut de cette Activity apparait une barre de recherche permettant de rechercher des films dans la base de données de 
__www.themoviedb.org__ , les résultats apparaissant sous la même forme de liste.

- En haut a gauche un menu permet d'accéder aux autres Activité `Coups de coeur` et `Films vus` en selectionnant l'une de ces activité on obtient la liste des films ainsi choisis.

- En appuyant sur la miniature d'un film s'ouvre l'activité de `Détails` du film selectionné, cette page regroupe les détails du film comme :
  - Son illustration
  - Sa date de sortie 
  - Sa note moyenne 
  - Son résumé
  - Sa bande annonce
  
Sur cette page il est également possible de marquer le film comme étant un coup de coeur ou comme déja vu grace aux boutons présents au bas de la page.
 
 
 
  
