#!/bin/bash
TMP_FOLDER=tmp-release;
MAVEN=`which mvn 2> /dev/null`;
if ! test "$?" = "0"; then
	echo "Cannot find maven";
	exit;
fi

$MAVEN clean install;
$MAVEN package -pl projewski-portfolio;

VERSION=`mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec`;

if test -e $TMP_FOLDER; then
	rm -rf $TMP_FOLDER;
fi

mkdir $TMP_FOLDER;
mkdir $TMP_FOLDER/projewski-bitcoin;
mkdir $TMP_FOLDER/projewski-bitcoin/jars;

if test -f projewski-portfolio/target/projewski-portfolio-$VERSION.jar; then
	cp projewski-portfolio/target/projewski-portfolio-$VERSION.jar $TMP_FOLDER/projewski-bitcoin/jars;
	cp -r projewski-portfolio/target/dependency-jars $TMP_FOLDER/projewski-bitcoin/jars;
	cp src/scripts/run-portfolio.sh $TMP_FOLDER/projewski-bitcoin;
fi

cd $TMP_FOLDER;
7z a -r ../projewski-bitcoin-$VERSION.7z projewski-bitcoin;
cd ..;

rm -rf $TMP_FOLDER;
