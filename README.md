# RealEstateManager
RealEstateManager est une application de gestion des biens immobiliers.  
L’agent immobilier peut créer un nouveau bien depuis l’application, en précisant tout ou partie des informations demandées.   
Une fois l'ajout d'un bien correctement effectué, un message de notification apparait sur le téléphone de l'utilisateur afin de lui indiquer que tout s'est bien passé.  
La géo-localisation d'un bien est automatiquement effectuée à partir de son adresse, afin d'afficher la vignette de carte correspondante dans le détail du bien.  
Les biens existants peuvent être édités pour mettre à jour leurs informations (ajout, modification, suppression).  
Il est possible de préciser qu’un bien a été vendu, en précisant sa date de vente.  


# Fonctionnalités

Un simulateur de prêt immobilier, avec un apport, un taux et une durée.  
Fonctionnalité de recherche textuelle ou par critères.  
Gestion de la géo-localisation.  
L’envoi de messages Push.  
Mode hors-ligne.  
Attributs d'un bien immobilier, pour chaque bien immobilier, les informations suivantes sont disponibles :  
-Le type de bien (appartement, loft, manoir, etc).   
-Le prix du bien (en dollar).   
-La surface du bien (en m2).   
-Le nombre de pièces.   
-La description complète du bien.   
-Au moins une photo, avec une description associée. La photo peut être récupérée depuis la galerie photos du téléphone, ou prise directement avec l'équipement.   
-L’adresse du bien.    
-Les points d’intérêts à proximité (école, commerces, parc, etc).   
-Le statut du bien (toujours disponible ou vendu).   
-La date d’entrée du bien sur le marché.   
-La date de vente du bien, s’il a été vendu.   
-L'agent immobilier en charge de ce bien.  

# Stack technique
  
Développement Android Natif Kotlin
Architectures : MVVM
Qualité : Clean Code, Tests unitaires, Tests fonctionnels
Outils : GIT, Gradle, Room, Okhttp, Retrofit, RxAndroid

[![Application.png](https://i.postimg.cc/W37GNFjs/Application.png)](https://postimg.cc/BLXLNvXz)
