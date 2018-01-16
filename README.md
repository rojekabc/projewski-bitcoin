# Projewski-Bitcoin

This project is the set of Java Libraries and Appications, which are helpfull in observe and manage bitcoin and other alt-coins.
Current project applcation, projewski-portfolio, has following features:
* add / remove watchers to exchange to observe coins
* add / remove transactions, which we wish to observe and use "stop win" (target alarm), "stop loose" (stop alarm), "moving stop" (move stop alarm)
* works as a terminal application, which uses Ansi (color linux terminal by default, windows with, for example, git-bash application)
* store last user configuration in the JSON file and loads it, when application starts again
* watch BitBay exchange

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
## Planned features / roadmap
Now I'm working on version 0.0.1. Current version is a snapshot. Issue backlog and milestone for version 0.0.1 is visible on the project's Issues tab.
