#!/bin/bash

OUT_DIR=./OUT_DIR
cd $(pwd)

mkdir -p ${OUT_DIR}

function _processSwagger {
    oc process -f swagger-ui.yaml -o yaml --param-file=swagger-ui.properties --ignore-unknown-parameters=true > ${OUT_DIR}/swagger-ui.yaml
}

function _processJenkins {
    oc process -f jenkins-bc.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins-bc.yaml
    oc process -f jenkins-slaves.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins-slaves.yaml
    oc process -f jenkins.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins.yaml
}

function createSwagger {
    _processSwagger
    oc apply -f ${OUT_DIR}/swagger-ui.yaml
}

function deleteSwagger {
    _processSwagger
    oc delete -f ${OUT_DIR}/swagger-ui.yaml
}

function createJenkins {
    _processJenkins
    oc create -f ${OUT_DIR}/jenkins-bc.yaml
    oc create -f ${OUT_DIR}/jenkins-slaves.yaml
    oc create -f ${OUT_DIR}/jenkins.yaml
}

function deleteJenkins {
    _processJenkins
    oc delete -f ${OUT_DIR}/jenkins-bc.yaml
    oc delete -f ${OUT_DIR}/jenkins-slaves.yaml
    oc delete -f ${OUT_DIR}/jenkins.yaml
}

function createAll {
    createSwagger 
}

function deleteAll {
    deleteSwagger 
}

${1}