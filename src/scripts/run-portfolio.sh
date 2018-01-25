#!/bin/bash

JAVA=`which java 2> /dev/null`;
if ! test "$?" = "0"; then
	echo "Cannot find java";
	exit;
fi

for i in `find . -name "projewski-portfolio-*.jar"`; do
	$JAVA -jar $i;
	exit;
done
