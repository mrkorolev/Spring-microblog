ARG MVN_V=3.9.8-amazoncorretto-17
ARG TOMCAT_V=9.0.91-jdk17-corretto

FROM maven:$MVN_V AS build
WORKDIR /build
COPY pom.xml pom.xml
COPY src/ src/
RUN mvn package -DskipTests
#RUN mvn package

FROM tomcat:$TOMCAT_V
RUN rm -rf /usr/local/tomcat/webapps
COPY --from=build /build/target/spring-microblog.war /usr/local/tomcat/webapps/spring-microblog.war
EXPOSE 8080
CMD ["catalina.sh", "run"]