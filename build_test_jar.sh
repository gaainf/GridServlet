#!/bin/bash

test=SeleniumGridServletTest
build_dir=build
src_dir=tests

mkdir -p build
if [ ! -f ${build_dir}/junit.jar ]; then
    wget -q http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar -O ${build_dir}/junit.jar
fi
if [ ! -f ${build_dir}/hamcrest-core.jar ]; then
    wget -q http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar -O ${build_dir}/hamcrest-core.jar
fi
if [ ! -f ${build_dir}/mockito.jar ]; then
    wget -q http://www.java2s.com/Code/JarDownload/mockito/mockito-all-1.9.5.jar.zip -O ${build_dir}/mockito.zip
    unzip -o ${build_dir}/mockito.zip -d ${build_dir}/
fi

#build class (it should be the current directory to keep all dependencies)
cd ${build_dir}
javac -cp "./*" ../${src_dir}/${test}.java -d ./
jar cvf ./${test}.jar ./${test}.class

