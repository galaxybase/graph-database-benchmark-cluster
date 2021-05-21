#!/bin/bash
mvn install:install-file -DgroupId=com.galaxybase -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=../galaxybase/lib/galaxybase-bolt-driver-3.0.1.jar
mvn -f ../pom.xml clean
mvn -f ../pom.xml package
rm -rf Twitter-2010/hugegraph.jar
cp target/benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar Twitter-2010/hugegraph.jar
java -jar Twitter-2010/hugegraph.jar
