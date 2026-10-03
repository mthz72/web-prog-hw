# Build WAR bang Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package

# Chay tren Tomcat
FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/ch07cart.war /usr/local/tomcat/webapps/ROOT.war

# Render cap port qua bien PORT
RUN sed -i 's/port="8080"/port="${port.http}"/' /usr/local/tomcat/conf/server.xml
ENV PORT=8080
EXPOSE 8080
CMD ["sh", "-c", "CATALINA_OPTS=\"-Dport.http=${PORT}\" exec catalina.sh run"]
