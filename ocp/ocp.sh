#!/bin/bash

OUT_DIR=./OUT_DIR
cd $(pwd)

mkdir ${OUT_DIR}

function processSwagger {
    oc process -f swagger.ui.yaml --parameters-file=swagger-ui.properties > ${OUT_DIR}/swagger-ui.json
}

function createSwagger {
    processSwagger
    oc create -f ${OUT_DIR}/swagger-ui.json
}

function deleteSwagger {
    processSwagger
    oc delete -f ${OUT_DIR}/swagger-ui.json
}

function createAll {
    createSwagger 
}

function deleteAll {
    deleteSwagger 
}

${1}