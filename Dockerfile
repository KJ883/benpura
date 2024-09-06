FROM amazoncorretto:21 AS build-env
ADD . /work
WORKDIR /work
RUN chmod +x gradlew
RUN ./gradlew build
FROM amazoncorretto:21-alpine
COPY --from=build-env /work/build/libs/*.jar /usr/local/bin/benpura.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/bin/benpura.jar"]