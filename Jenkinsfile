pipeline {
  agent any
  stages {
    stage("Build") {
      steps {
        script{
          podTemplate(cloud: 'openshift', label: 'mega-maven-pod', containers: [
            containerTemplate(name: 'mega-maven-container', image: 'docker.io/maven:3.6.2-jdk-11-slim', ttyEnabled: true, command: 'cat')
          ],
          envVars: [
            envVar(key: 'JAVA_MAX_HEAP_PARAM', value: '-Xmx1g'),
            envVar(key: 'CONTAINER_CORE_LIMIT', value: '1')
          ],
          volumes: [
            persistentVolumeClaim(claimName: 'jenkins-mvn-repo-cache', mountPath: '/home/jenkins/.m2/repository')
          ]) {

            node('mega-maven-pod') {
              stage('Build a Maven project') {
                container('mega-maven-container') {
                    git url: 'https://github.com/cchet-gepardec/mega.git', branch: "${env.GIT_BRANCH}", credentialsId: 'github-login'
                    sh 'mvn -B -s jenkins-settings.xml clean install'
                }
              }
            }
          }
        }
      }
    }
  }
}