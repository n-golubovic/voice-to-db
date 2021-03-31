FROM adoptopenjdk/openjdk15:jdk-15.0.2_7-centos-slim

RUN yum -y update
RUN yum -y groupinstall "Development Tools"
RUN yum -y install openssl-devel bzip2-devel libffi-devel
RUN yum -y install wget
RUN wget https://www.python.org/ftp/python/3.8.3/Python-3.8.3.tgz
RUN tar xvf Python-3.8.3.tgz
RUN cd /Python-3.8.3 && ./configure --enable-optimizations
RUN cd /Python-3.8.3 && make altinstall
RUN pip3.8 install vosk

COPY target/voice-to-db-1.0.0.jar /app/app.jar
COPY python/vosk_voice.py /app/vosk_voice.py
COPY python/model /app/model

RUN mkdir /results
RUN mkdir /upload

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
