#!groovy

pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    stages {
        stage("Maven-Agent") {
            steps {
                script {
                    stash name: "repo", includes: '**'
                    podTemplate(cloud: 'openshift', label: 'quarkus-build-agent', serviceAccount: 'jenkins', containers: [
                            containerTemplate(name: 'quarkus-build', image: 'docker-registry.default.svc:5000/57-mega-dev/quarkus-build-agent:latest', ttyEnabled: true, command: 'cat', alwaysPullImage: true)
                    ],
                            envVars: [
                                    envVar(key: 'JAVA_MAX_HEAP_PARAM', value: '-Xmx1g'),
                                    envVar(key: 'CONTAINER_CORE_LIMIT', value: '1')
                            ],
                            volumes: [
                                    persistentVolumeClaim(claimName: 'jenkins-mvn-repo-cache', mountPath: '/home/jenkins/.m2/repository')
                            ]) {

                        node('quarkus-build-agent') {
                            container('quarkus-build') {
                                unstash name: "repo"
                                stage('Build') {
                                    def revision = buildVersionForBranch()
                                    sh "mvn -B -s jenkins-settings.xml clean install \
                                            -Drevision=${revision} \
                                            -Dquarkus.package.uber.jar=true\
                                            -DskipTests=true"
                                }

                                stage('Deploy') {
                                    dir('mega-zep-backend/target/') {
                                        script {
                                            openshift.withCluster() {
                                                openshift.withProject("57-mega-dev") {
                                                    def revision = buildVersionForBranch()
                                                    def ocpApp = "mega-zep-bakend-${revision.toLowerCase()}"
                                                    def artifact = "mega-zep-backend-${revision}-runner.jar"
                                                    def label = "version=${ocpApp}"
                                                    sh "oc start-build mega-zep-backend --from-file=${artifact} --follow --wait \
                                                        && oc tag mega-zep-backend:latest mega-zep-backend:${revision}"
                                                    deleteOcpApp(ocpApp, label, true)
                                                    try {
                                                        sh "oc new-app mega-zep-backend:${revision} \
                                                                      --labels ${label} \
                                                                      --name=${ocpApp} \
                                                                      --env JAVA_MAX_MEM_RATIO=95 \
                                                                      --env AB_JOLOKIA_HTTPS=true \
                                                                      --env AB_PROMETHEUS_OFF=true \
                                                                      --env JAVA_OPTIONS=-Djava.net.preferIPv4Stack=true \
                                                        && oc set triggers dc/${ocpApp} --remove-all \
                                                        && oc expose svc/${ocpApp} --name=${ocpApp} --port=8080 --labels ${label}"
//                                                                && oc set probe dc/${ocpApp} --readiness --failure-threshold 5 --initial-delay-seconds 2 --get-url=http://localhost:8080/health/ready \
                                                    } catch (exception) {
                                                        error exception.getMessage()
                                                        deleteOcpApp(ocpApp, label)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

def deleteOcpApp(String ocpApp, String selector, boolean wait = false) {
    sh "oc delete all --selector ${selector} --ignore-not-found=true \
        && oc delete is/${ocpApp} --ignore-not-found=true"
    if (wait) {
        def removed = false;
        timeout(time: 33, unit: "SECONDS") {
            while (!removed) {
                removed = !openshift.selector("dc", ocpApp).exists() && !openshift.selector("svc", ocpApp).exists() \
                           && !openshift.selector("is", ocpApp).exists() \
                           && !openshift.selector("route", ocpApp).exists()
                sleep time: 2, unit: "SECONDS"
            }
        }
        if (!removed) {
            error "Could not delete application"
        }
    }
}

def buildVersionForBranch(String pomLocation = "./") {
    def branch = "${env.GIT_BRANCH}".trim().toLowerCase()
    def revision = "";
    if (branch.equals("develop")) {
        pom = readMavenPom file: pomLocation + 'pom.xml'
        return pom.properties['revision']
    } else if (branch.startsWith("feature/")) {
        return branch.replace("/", "-").toUpperCase()
    } else if (branch.startsWith("pr-")) {
        return branch.replace("/", "-").toUpperCase()
    } else if (branch.startsWith("release/") || branch.startsWith("hotfix/")) {
        pom = readMavenPom file: pomLocation + 'pom.xml'
        return pom.properties['revision']
    } else {
        error("Branch not detected. branch=${branch}")
    }
}

def isPullRequest() {
    def branch = "${env.GIT_BRANCH}".trim().toLowerCase()
    return branch.startsWith("pr-")
}