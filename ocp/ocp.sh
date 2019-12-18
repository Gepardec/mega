#!/bin/bash

OUT_DIR=./OUT_DIR
cd $(pwd)

set -u

mkdir -p ${OUT_DIR}

function _processSonarqube {
    oc process -f sonarqube.yaml -o yaml --param-file=sonarqube.properties --ignore-unknown-parameters=true > ${OUT_DIR}/sonarqube.yaml
}

function recreateSecret {
    deleteSecret
    createSecret
}

function createJenkins {
    oc process -f jenkins-bc.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true | oc apply -f -
    oc process -f jenkins.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true | oc apply -f -
}

function deleteJenkins {
    oc process -f jenkins-bc.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true | oc delete -f -
    oc process -f jenkins.yaml -o yaml --param-file=jenkins.properties --ignore-unknown-parameters=true | oc delete -f -
}

function recreateJenkins {
    deleteJenkins
    createJenkins
}

function createSonarqube {
    oc process -f sonarqube.yaml -o yaml --param-file=sonarqube.properties --ignore-unknown-parameters=true | oc apply -f -
}

function deleteSonarqube {
    oc process -f sonarqube.yaml -o yaml --param-file=sonarqube.properties --ignore-unknown-parameters=true | oc delete -f -
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