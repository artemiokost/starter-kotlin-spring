FROM gradle:8.10-jdk21 as builder
LABEL stage=build
ARG service=core-service
ARG profile=prod
USER root
WORKDIR /workspace
COPY ./$service ./$service
COPY ./common ./common
COPY ./gradle/libs.versions.toml ./gradle/libs.versions.toml
COPY ./settings.gradle.kts ./settings.gradle.kts
RUN gradle $service:bootJar

FROM amazoncorretto:21.0.4-alpine
ARG service=core-service
ARG profile=prod
ENV ENV_SERVICE $service
ENV ENV_PROFILE $profile
WORKDIR /opt/app
COPY --from=builder /workspace/$service/build/libs/ /opt/app

CMD java -jar $ENV_SERVICE.jar --spring.profiles.active=$ENV_PROFILE
