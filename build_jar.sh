#!/bin/bash

servlet=GridServlet

mkdir -p build
wget -q https://selenium-release.storage.googleapis.com/3.4/selenium-server-standalone-3.4.0.jar -O build/selenium-server-standalone.jar
wget -q http://www.java2s.com/Code/JarDownload/javax.servlet/javax.servlet.jar.zip -O build/javax.servlet.jar.zip
unzip -o build/javax.servlet.jar.zip -d build/
wget -q http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip -O build/java-json.jar.zip
unzip -o build/java-json.jar.zip -d build/

#build class
javac -cp "build/selenium-server-standalone-.jar:build/*" src/${servlet}.java -d build/
jar cvf build/${servlet}.jar build/${servlet}.class
