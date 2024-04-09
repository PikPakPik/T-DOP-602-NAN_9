FROM jenkins/jenkins

WORKDIR /var/jenkins_home

# Installation des dépendances pour les tests
USER root
RUN apt-get update && apt-get install build-essential libcriterion-dev git -y
USER jenkins

# Configuration de Jenkins
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc.yaml
COPY ./my_marvin.yml /var/jenkins_home/casc.yaml

# Configuration des jobs
COPY job_dsl.groovy /var/jenkins_home/job_dsl.groovy

# Installation des plugins Jenkins
COPY ./plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

EXPOSE 50000
EXPOSE 8080
