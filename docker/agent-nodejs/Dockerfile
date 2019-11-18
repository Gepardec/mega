FROM docker.io/openshift/jenkins-slave-base-centos7:v3.9

ENV NPM_CONFIG_PREFIX=/npm/.npm-global \
    PATH=/npm/node_modules/.bin/:/npm/.npm-global/bin:$PATH

USER root

WORKDIR /

# Install NodeJS
RUN curl -sL https://rpm.nodesource.com/setup_12.x | bash - \
    && yum install -y gcc c++ make nodejs-12.5.0 \
    && npm install -g @angular/cli \
    && yum clean all -y \
    && rm -rf /var/cache/yum \
    && chown 1001:0 -R /npm

WORKDIR /home/jenkins

USER 1001