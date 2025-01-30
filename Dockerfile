FROM openjdk:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл в контейнер
COPY target/prepJournal-0.0.1-SNAPSHOT.jar prepJournal-0.0.1-SNAPSHOT.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "prepJournal-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080