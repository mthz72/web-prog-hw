# =========================================================
#  Stage 1: build file WAR bang Maven
# =========================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom truoc va tai dependency rieng mot lop.
# Sua code ma khong sua pom -> Docker dung lai cache, build nhanh hon nhieu.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package

FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/ch07cart.war /usr/local/tomcat/webapps/ROOT.war

RUN sed -i 's/port="8080"/port="${port.http}"/' /usr/local/tomcat/conf/server.xml

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "export CATALINA_OPTS=\"-Dport.http=${PORT:-8080}\"; exec catalina.sh run"]
