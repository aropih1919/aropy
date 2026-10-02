#!/bin/bash
#Compilation
mkdir -p build
find . -name "*.java" > sources.txt
javac -cp "lib/*" -d build @sources.txt
rm sources.txt
#Creation jar
jar cf aropy-framework2.jar -C build .
sudo cp -f aropy-framework2.jar /home/aropih/MpiaroS4/M.Naina/WebDynamique/framework/test/lib