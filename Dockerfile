
FROM openjdk:21-jdk-slim

RUN apt update && apt install -y openssh-client bash && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/prepJournal-1.0.0.jar prepJournal-1.0.0.jar

ENTRYPOINT ["java", "-jar", "prepJournal-1.0.0.jar"]

EXPOSE 8080