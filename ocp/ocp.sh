#!/bin/bash

OUT_DIR=./OUT_DIR
cd $(pwd)

set -u

mkdir -p ${OUT_DIR}

function _processJenkins {
    oc process -f jenkins-bc.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins-bc.yaml
    oc process -f jenkins-slaves.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins-slaves.yaml
    oc process -f jenkins.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true > ${OUT_DIR}/jenkins.yaml
}

function _processSonarqube {
    oc process -f sonarqube.yaml -o yaml --param-file=sonarqube.properties --ignore-unknown-parameters=true > ${OUT_DIR}/sonarqube.yaml
}

function recreateSecret {
    deleteSecret
    createSecret
}

function createJenkins {
    _processJenkins
    oc apply -f ${OUT_DIR}/jenkins-bc.yaml
    oc apply -f ${OUT_DIR}/jenkins-slaves.yaml
    oc apply -f ${OUT_DIR}/jenkins.yaml
}

function deleteJenkins {
    _processJenkins
    oc delete -f ${OUT_DIR}/jenkins-bc.yaml
    oc delete -f ${OUT_DIR}/jenkins-slaves.yaml
    oc delete -f ${OUT_DIR}/jenkins.yaml
}

function recreateJenkins {
    deleteJenkins
    createJenkins
}

function createSonarqube {
    _processSonarqube
    oc apply -f ${OUT_DIR}/sonarqube.yaml
}

function deleteSonarqube {
    _processSonarqube
    oc delete -f ${OUT_DIR}/sonarqube.yaml
}

function recreateSonarqube {
    deleteSonarqube
    createSonarqube
}

function createAll {
    createJenkins
    createSonarqube
}

function deleteAll {
    deleteJenkins
    deleteSonarqube
}

function recreateAll {
    recreateJenkins
    recreateSonarqube
}

${1}