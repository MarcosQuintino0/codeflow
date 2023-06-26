FROM maven:3.8.6-amazoncorretto-11 as build_jar

COPY pom.xml .
RUN mvn dependency:resolve

COPY src src
RUN mvn package -DskipTests

FROM maven:3.8.6-amazoncorretto-11
ARG JAR_FILE=target/*.jar
COPY --from=build_jar ${JAR_FILE} bugsolver-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "bugsolver-0.0.1-SNAPSHOT.jar"]