FROM docker.io/openshift/jenkins-slave-base-centos7:v3.9

ENV NPM_CONFIG_PREFIX=/npm/.npm-global \
    PATH=/npm/node_modules/.bin/:/npm/.npm-global/bin:$PATH

USER root

# Install NodeJS
RUN curl -sL https://rpm.nodesource.com/setup_12.x | bash - \
    && yum install -y gcc c++ make nodejs-12.5.0 \
    && npm install -g @angular/cli \
    && yum clean all -y \
    && rm -rf /var/cache/yum \
    && chown -R 1001:0 /npm \
    && chown -R 1001:0 /home/jenkins \
    && chmod -R 770 /npm \
    && chmod -R 770 /home/jenkins

USER 1001