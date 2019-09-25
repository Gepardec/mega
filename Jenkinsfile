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
              git url: "${env.GIT_URL}", branch: "${env.GIT_BRANCH}", credentialsId: 'github-login'
              container('mega-maven-container') {
                  sh 'mvn -B -s jenkins-settings.xml clean install'
                  stash name: "mega-zep-frontend", include: "mega-zep-frontend-*.jar"
                  stash name: "mega-zep-backend", include: "mega-zep-backend-*.jar"
              }
            }
          }
        }
      }
    }

    stage('Deploy') {
      steps{
        openshift.withCluster() {
          openshift.withProject("mega-dev") {
            unstash: "mega-zep-frontend"
            unstash: "mega-zep-backend"
            sh 'ls -lrta'
            //openshift.selector("bc", "tasks").startBuild("--from-file=./target/openshift-tasks.war", "--wait=true")
            //openshift.tag("tasks:latest", "tasks:${devTag}")
          }
        }
      }
    }
  }
}