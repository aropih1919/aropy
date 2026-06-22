#!/bin/bash
#Compilation
mkdir -p build
find . -name "*.java" > sources.txt
javac -cp "lib/*" -d build @sources.txt
rm sources.txt
#Creation jar
jar cf aropy-framework2.jar -C build .
sudo cp -f aropy-framework2.jar /usr/share/tomcat10/lib