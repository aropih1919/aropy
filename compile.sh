#!/bin/bash
#Compilation
mkdir -p build
find . -name "*.java" > sources.txt
javac -cp "lib/*" -d build @sources.txt
rm sources.txt
#Creation jar
jar cf aropy-framework.jar -C build .
sudo cp aropy-framework.jar /usr/share/tomcat10/lib