# MyERP
* Taux de couverture du projet : 91%

## Organisation du répertoire

*   `doc` : documentation
*   `docker` : répertoire relatifs aux conteneurs _docker_ utiles pour le projet
    *   `dev` : environnement de développement
*   `src` : code source de l'application


## Environnement de développement

Les composants nécessaires lors du développement sont disponibles via des conteneurs _docker_.
L'environnement de développement est assemblé grâce à _docker-compose_
(cf docker/dev/docker-compose.yml).

Il comporte :

*   une base de données _PostgreSQL_ contenant un jeu de données de démo (`postgresql://127.0.0.1:9032/db_myerp`)


## Initialisation du projet

### 1 - Docker : 

Dans le dossier docker/dev modifiez votre adresse IP correspondant à votre environnement depuis le fichier `docker-compose.yml`

`ports:
    - "[votre adresse ip]:9032:5432"`

Depuis votre environnement Docker :

Positionnez-vous dans le répertoire du conteneur : cd /docker/dev

#### Lancement

    docker-compose up


#### Arrêt

    docker-compose stop


#### Remise à zero

    docker-compose stop (si vous n'avez pas déjà arreté votre conteneur)
    docker-compose rm -v
    docker-compose up


### 2 - Properties DataBase :

Modifier l'adresse ip que vous avez paramétré dans docker

 `- /myerp-consumer/src/main/resources/database.properties :`

    myerp.datasource.driver=org.postgresql.Driver
   
    myerp.datasource.url=jdbc:postgresql://[votre adresse ip]:9032/db_myerp
   
    myerp.datasource.username=usr_myerp
   
    myerp.datasource.password=myerp`

### 3 - Tests Unitaires :

Afin de vérifier le taux de couverture, on s'appuie sur jacoco.

Les profiles à activer sont :

` - test-business`
` - test-consumer `

Lancement avec la commande maven :

`- mvn clean verify `

Lancement avec Jenkins (à condition de l'avoir paramétrer au préalable, sinon se referer à la configuration de Jenkins plus bas)

Pour voir le taux de couverture :

`- soit par votre navigateur en ouvrant les fichiers :
    - myerp-business/target/site/jacoco-aggregate/index.html
    - myerp-consumer/target/site/jacoco-aggregate/index.html
    - myerp-model/target/site/jacoco-aggregate/index.html`
    
`- soit avec jenkins`

## Configuration de Jenkins

Après avoir installé Jenkins ( https://www.jenkins.io/doc/book/installing/), il est conseillé de
sauvegarder entre chaque étape.

1) Créer un nouveau projet freestyle

2) Dans l'onglet Général, cocher GitHub project et renseigner l'adresse URL du repository git

3) Dans l'onglet Gestion de code source: cocher Git, entrer l'URL du repository git et dans la case Branch Specifier renseignez `*/master`

4) Dans l'onglet Ce qui déclenche le build, cocher Scrutation de l'outil de version  
    et entrer `* * * *`  
    
5) Dans l'onglet Build: 
	Ajouter une étape au build: 
		Invoquer une cible de haut niveau  
    -choisir une version de maven (le plugin maven doit être installé)  
    -enter dans Cibles Maven: `clean install -Ptest-business,test-consumer`. La commande ci-dessus lance un build de maven avec le clean install, -P sert à  
 sélectionner les profils permettant de lancer les tests 
        
6) Dans l'onglet Actions à la suite du build: 
    - Ajouter une action après le buid: Publier le rapport des résultats des test JUnit 
    [vérifier que XML des rapports de test contient:`**/target/surefire-reports/*.xml`  
    et que le Health report amplification factor est à 1.0]  
    - De nouveau ajouter une action après le build: Record Jacoco coverage report  
    [vérifier que Path to exec files (e.g.: **/target/**.exec, **/jacoco.exec) soit à:  
    **/**.exec , que Path to class directories (e.g.: **/target/classDir, **/classes) soit à  
    **/classes , que Path to source directories (e.g.: **/mySourceFiles) soit à: **/src/main/java  ,
    que Inclusions (e.g.: **/*.java,**/*.groovy,**/*.gs) soit à: **/*.java,**/*.groovy,**/*.kt,**/*.kts]