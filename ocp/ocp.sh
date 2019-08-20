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

function _processSonarqube {
    oc process -f sonarqube.yaml -o yaml --param-file=sonarqube.properties --ignore-unknown-parameters=true > ${OUT_DIR}/sonarqube.yaml
}

function createSecret {
    oc secrets new-sshauth github-ssh --ssh-privatekey="../github_id_rsa"
    oc label secret github-ssh credential.sync.jenkins.openshift.io=true
}

function deleteSecret {
    oc delete secret/github-ssh
}

function recreateSecret {
    deleteSecret
    createSecret
}

function createSwagger {
    _processSwagger
    oc apply -f ${OUT_DIR}/swagger-ui.yaml
}

function deleteSwagger {
    _processSwagger
    oc delete -f ${OUT_DIR}/swagger-ui.yaml
}

function recreateSwagger {
    deleteSwagger
    createSwagger
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
    createSwagger 
    createJenkins
    createSonarqube
}

function deleteAll {
    deleteSwagger 
    deleteJenkins
    deleteSonarqube
}

function recreateAll {
    recreateSwagger 
    recreateJenkins
    recreateSonarqube
}

${1}