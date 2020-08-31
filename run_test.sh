#!/bin/bash

#java -cp "build/*" org.junit.runner.JUnitCore SeleniumGridServletTest
#only integration    
#mvn clean -Dtest=*Integration* test
#all tests
mvn validate
mvn test
