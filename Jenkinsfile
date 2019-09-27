pipeline {
  agent any
   
  options {
    disableConcurrentBuilds()
  }

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
                  def revision=buildVersionForBranch()
                  sh "mvn -B -s jenkins-settings.xml clean install -Drevision=${revision}"
                  stash name: "mega-zep", includes: "**/mega-zep-*.jar"
              }
            }
          }
        }
      }
    }

    stage('Deploy') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject("mega-dev") {
              unstash: "mega-zep-frontend"
              //openshift.selector("bc", "tasks").startBuild("--from-file=./target/openshift-tasks.war", "--wait=true")
              //openshift.tag("tasks:latest", "tasks:${devTag}")
            }
          }
        }
      }
    }
  }
}

def buildVersionForBranch(String pomLocation="./") {
    def branch="${env.GIT_BRANCH}".trim().toLowerCase()
    if (branch.equals("develop")) {
        echo "Develop found"
        pom = readMavenPom file: pomLocation + 'pom.xml'
        return pom.properties['revision'] + "-SNAPSHOT"
    }
    else if (branch.startsWith("feature/")) {
        echo "Feature found"
        return branch.replace("/", "-").toUpperCase() + "-SNAPSHOT"
    }
    else if (branch.startsWith("release/") || branch.startsWith("hotfix/")) {
        echo "Release or Hotfix found"
        pom = readMavenPom file: pomLocation + 'pom.xml'
        return pom.properties['revision']
    } else{
        echo "Nothing found"
    }
}