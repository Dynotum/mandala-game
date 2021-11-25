FROM openjdk:11

COPY . /code

WORKDIR /code
RUN chmod 755 gradlew
RUN ./gradlew clean
RUN ./gradlew executeYarn
RUN ./gradlew bootJar

ENTRYPOINT ["java", "-jar", "build/libs/malanca-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080