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

# =========================================================
#  Stage 2: chay WAR tren Tomcat
# =========================================================
FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/ch14email.war /usr/local/tomcat/webapps/ROOT.war

# Pool khai bao trong META-INF/context.xml do TOMCAT tao ra, nen driver JDBC
# phai nam o tomcat/lib chu khong chi trong WEB-INF/lib cua war.
# Copy ca hai: postgresql dung tren Render, mysql de chay thu o may.
# (Hibernate va jakarta.mail thi nguoc lai - chung do ung dung dung nen cu
#  de trong WEB-INF/lib cua war.)
COPY --from=build /app/target/ch14email/WEB-INF/lib/postgresql-*.jar /usr/local/tomcat/lib/
COPY --from=build /app/target/ch14email/WEB-INF/lib/mysql-connector-j-*.jar /usr/local/tomcat/lib/

# Render cap port qua bien PORT, Tomcat lai doc tu server.xml -> doi thanh
# mot system property de entrypoint truyen vao.
RUN sed -i 's/port="8080"/port="${port.http}"/' /usr/local/tomcat/conf/server.xml

COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
