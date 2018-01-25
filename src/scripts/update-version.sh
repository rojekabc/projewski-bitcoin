#!/bin/bash
MAVEN=`which mvn 2> /dev/null`;
if ! test "$?" = "0"; then
	echo "Cannot find maven";
	exit;
fi

$MAVEN versions:set
