FROM docker.io/openshift/jenkins-slave-base-centos7:v3.9 

ENV PATH=$PATH:/maven/bin
ENV MAVEN_HOME=/maven \
    M2_HOME=/maven

USER root 

RUN yum remove -y java-1.8.0-openjdk* \
    && yum install -y java-11-openjdk-devel \
    && yum clean all \
    && rm -rf /var/cache/yum \
    && mkdir /maven \
    && curl -L --silent --show-error --fail https://archive.apache.org/dist/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz \
         | tar --strip-components=1 -zx -C /maven

USER 1001