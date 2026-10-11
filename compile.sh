#!/bin/bash

set -e
SCRIPT_DIR="$(cd -- "$(dirname -- "$0")" && pwd)"
cd "$SCRIPT_DIR"

mkdir -p build
SOURCES_FILE="$(mktemp)"
trap 'rm -f "$SOURCES_FILE"' EXIT
find mg -name "*.java" -print > "$SOURCES_FILE"

javac -parameters -cp "lib/*" -d build @"$SOURCES_FILE"
jar cf aropy-framework2.jar -C build .
cp -f aropy-framework2.jar "$SCRIPT_DIR/../../test/lib/"

echo "Compilation terminée avec -parameters; JAR copié dans test/lib."
