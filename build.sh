#!/usr/bin/env bash

pushd client
ng build -prod
popd

mkdir -p server/src/main/resources/frontend
cp -R client/dist/* server/src/main/resources/frontend

sbt server/universal:packageBin
