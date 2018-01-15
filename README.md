# Projewski-Bitcoin

This project is the set of Java Libraries and Appications, which are helpfull in observe and manage bitcoin and other alt-coins.

## Getting started

There is application module called projewski-portfolio, which is builded from whole project components.
### Prerequisites
To compile and run this application you need
* Java SDK 8
* Maven
* Git
### Compilation and run
* Download project local to your machine
```
git clone https://github.com/rojekabc/projewski-bitcoin
```
* From the main project directory compile and install all modules
```
mvn install
```
* Enter to projewski-portfolio module folder and create a package by the command
```
mvn package
```
* Inside folder target there will be jar file and dependency-jars folder. They should be always kept together. To run application put the command
```
java -jar projewski-portfolio-*.jar
```
