#!/bin/bash

OUT_DIR=./OUT_DIR
cd $(pwd)

mkdir ${OUT_DIR}

function _processSwagger {
    oc process -f swagger-ui.yaml --parameters-file=swagger-ui.properties > ${OUT_DIR}/swagger-ui.json
}

function _processJenkins {
    oc process -f jenkins-bc.yaml --parameters-file=jenkins.properties > ${OUT_DIR}/jenkins-bc.json
    oc process -f jenkins-slaves.yaml --parameters-file=jenkins.properties > ${OUT_DIR}/jenkins-slaves.json
}

function createSwagger {
    _processSwagger
    oc create -f ${OUT_DIR}/swagger-ui.json
}

function deleteSwagger {
    _processSwagger
    oc delete -f ${OUT_DIR}/swagger-ui.json
}

function createJenkins {
    _processJenkins
    oc create -f ${OUT_DIR}/jenkins-bc.json
    oc create -f ${OUT_DIR}/jenkins-slaves.json
}

function deleteJenkins {
    _processJenkins
    oc delete -f ${OUT_DIR}/jenkins-bc.json
    oc delete -f ${OUT_DIR}/jenkins-slaves.json
}

function deleteJenkinsPv {
    
}

function createAll {
    createSwagger 
}

function deleteAll {
    deleteSwagger 
}

${1}