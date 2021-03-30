FROM adoptopenjdk/openjdk15:jdk-15.0.2_7-debian-slim

RUN apt-get update
RUN apt-get -y install python3
RUN apt-get -y install python3-pip

COPY target/voice-to-db-1.0.0.jar /app/app.jar
COPY python/vosk_voice.py /app/vosk_voice.py
COPY python/model /app/model

RUN mkdir /results
RUN mkdir /upload

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
