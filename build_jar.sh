#!/bin/bash

servlet=SeleniumGridServlet
build_dir=build
src_dir=src

mkdir -p build
if [ ! -f ${build_dir}/selenium-server-standalone.jar ]; then
    wget -q https://selenium-release.storage.googleapis.com/3.4/selenium-server-standalone-3.4.0.jar -O ${build_dir}/selenium-server-standalone.jar
fi
if [ ! -f ${build_dir}/javax.servlet.jar ]; then
    wget -q http://www.java2s.com/Code/JarDownload/javax.servlet/javax.servlet.jar.zip -O ${build_dir}/javax.servlet.jar.zip
    unzip -o ${build_dir}/javax.servlet.jar.zip -d ${build_dir}/
fi
if [ ! -f ${build_dir}/java-json.jar ]; then
    wget -q http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip -O ${build_dir}/java-json.jar.zip
    unzip -o ${build_dir}/java-json.jar.zip -d ${build_dir}/
fi

#build class (it should be the current directory to keep all dependencies)
cd ${build_dir}
javac -cp "./*" ../${src_dir}/${servlet}.java -d ./
jar cvf ./${servlet}.jar ./${servlet}.class

#build class
#javac -cp "${build_dir}/*" ${src_dir}/${servlet}.java -d ${build_dir}/
#jar cvf ${servlet}.jar ${build_dir}/${servlet}.class

