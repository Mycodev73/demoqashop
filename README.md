## Tests Automatiques du site http://shop.demoqa.com


### Technologies utilisées:

* Selenium WebDriver
* Java SE 1.8
* Maven
* TestNG
* WebDriverManager
* devskiller/jfairy

### Prérequis

* Java 1.8
* Maven


### Comment lancer les tests:

1. télécharger et désarchiver le fichier zip

2. lancer une console

2. allez dans le répertoire nouvellement créé

3. parcourrez le repertoire, les dossiers et fichiers suivants devraient s'afficher:

* src
* pom.xml
* README.md

si vous avez uniquement un dossier avec le même nom que le dossier parent, 
veuillez entrer dans ce dossier et de nouveau parcourrir les dossiers et fichiers.
Cette fois vous devriez avoir au moins le dossier et les 2 fichiers précédemment cités.

3. lancez la commande maven suivante :

```
mvn test -Dtest=org.mycodev.demoqashop.MainPageTest
```

