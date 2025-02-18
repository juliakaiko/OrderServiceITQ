# Используем образ Tomcat с Java 17.
FROM tomcat:9.0-jdk17

# Удаляем стандартные приложения Tomcat (опционально)
#RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR-файл в папку webapps Tomcat
COPY target/myproject-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Открываем порт, на котором будет работать Tomcat
EXPOSE 8080

# Команда для запуска Tomcat
CMD ["catalina.sh", "run"]