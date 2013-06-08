#!/bin/sh
cd kissanijar
#mvn clean package install
mvn install
cd ..
cd kissaniwar
mvn clean validate gwt:compile package gae:enhance gae:deploy
cd ..

