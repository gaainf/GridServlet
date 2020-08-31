#! /bin/bash

mvn validate
#skip tests
#mvn package -Dmaven.test.skip=true
mvn package
