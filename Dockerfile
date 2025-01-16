FROM bellsoft/liberica-openjdk-alpine:latest AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.m2  chmod +x mvnw && ./mvnw clean install -Dmaven.test.skip

FROM bellsoft/liberica-openjre-debian:23.0.1 AS layers
WORKDIR /application
COPY --from=builder /application/target/*.jar app.jar
RUN apt-get update && apt-get install -y ffmpeg
RUN java -Djarmode=layertools -jar app.jar extract

FROM bellsoft/liberica-openjre-debian:23.0.1
VOLUME /tmp
RUN useradd -ms /bin/bash spring-user
RUN apt-get update && apt-get install -y ffmpeg
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
