FROM openshift3/jenkins-slave-base-rhel7

ARG VERSION

# Environment variables
ENV MAVEN_HOME=/maven-${VERSION} \
    MVN_HOME=/maven-${VERSION} \
    M2_HOME=${HOME}/.m2 \
    BASH_ENV=/usr/local/bin/scl_enable \
    ENV=/usr/local/bin/scl_enable \
    PROMPT_COMMAND=". /usr/local/bin/scl_enable"
ENV PATH="${PATH}:${MAVEN_HOME}/bin"

ENV JAVA_HOME="/opt/oracle-jdk8"
ENV PATH="${PATH}:${JAVA_HOME}/bin"

USER root

# Add default settigns.xml
COPY contrib/settings.xml ${M2_HOME}/settings.xml
COPY contrib/configure-slave /usr/local/bin/configure-slave
COPY contrib/configure-slave /usr/local/bin/scl_enable

# Make directories
RUN mkdir -p ${MAVEN_HOME} \
    && mkdir -p ${M2_HOME}/repository \
    && yum install -y java-11-openjdk-devel \
    && curl -L http://us.mirrors.quenda.co/apache/maven/maven-3/${VERSION}/binaries/apache-maven-${VERSION}-bin.tar.gz \
      | tar xz --strip-components=1 -C ${MAVEN_HOME} \
    && chown -R 1001:1001 ${MAVEN_HOME} \
    && chmod -R 777 ${MAVEN_HOME} \
    && chmod -R 777 ${M2_HOME} \
    && yum clean all -y \
    && yum autoremove -y

USER 1001
