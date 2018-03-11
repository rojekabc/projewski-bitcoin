# Projewski-Bitcoin

This project is the set of Java Libraries and Appications, which are helpfull in observe and manage bitcoin and other alt-coins.

## Features
* Watch the BitBay exchange
* Watch the Binance exchange
* Manage a coin market watcher
* Manage the coin transaction list
* Terminal user interface
  * market watch management
  * transaction watch management
  * exchange switch
* Update information from coin markets
* Store and load configuration of watchers and transactions
* Assign to transaction "stop win", "stop loose" and "stop moving loose"
* Observe market changes through whole stored data and between last read information from market
* Bet on crypto-games service
* [Configuration](https://github.com/rojekabc/projewski-bitcoin/wiki/Configuration-properties) in one file of all modules

## Getting started
To run the application
* go to the [releases page](https://github.com/rojekabc/projewski-bitcoin/releases)
* choose your release (the best last one stable) and download the 7-Zip archive file
* extract the 7-Zip archive file
* enter the projewski-bitcoin folder
  * On Linux run script _run-portfolio.sh_
  * On Windows enter jars folder and double click projewski-portfolio-{VERSION}.jar
  
## Compile and run
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
bsh src/scripts/build.sh
```
* From the main project directory run portfolio application
```
bash src/scripts/run-portfolio.sh
```

## Planned features / roadmap
Currenlty planned version is 0.0.3. Look to the [issue page](https://github.com/rojekabc/projewski-bitcoin/milestone/4) to see, what is planned for the current milestone.
