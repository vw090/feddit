FROM maven:latest AS build

WORKDIR /backend
COPY pom.xml ./
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM eclipse-temurin:17

COPY --from=build /backend/target/backend.jar .
EXPOSE 8080
USER 1000:1000

ENTRYPOINT ["java","-jar","backend.jar"]
