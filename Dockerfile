FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY gradlew /app/gradlew
COPY src /app/src

# Execute o Gradle para construir o projeto
RUN ./gradlew build

FROM openjdk:21

EXPOSE 9000

WORKDIR /app

ENV DATABASE_CONNECTION_URL=""
ENV SCOPE="prod"
ENV PASSWORD=""
ENV USER=""

COPY --from=build /app/build/libs/fbills.jar /app/fbills.jar

CMD mkdir /app/files

ENTRYPOINT ["java", "-jar", "fbills.jar"]
