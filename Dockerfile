FROM openjdk:21

EXPOSE 9000
WORKDIR /app

ENV DATABASE_CONNECTION_URL=""
ENV SCOPE="prod"
ENV PASSWORD=""
ENV USER=""

COPY build/libs/fbills.jar /app/fbills.jar
CMD mkdir /app/files

ENTRYPOINT ["java", "-jar", "fbills.jar"]
