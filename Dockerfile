FROM eclipse-temurin:17
LABEL org.opencontainers.image.source=https://github.com/gammaStrahlung/monopoly_server
VOLUME /tmp
COPY build/libs/monopoly_server-*.jar monopoly_server.jar
ENTRYPOINT ["java", "-jar", "monopoly_server.jar"]
EXPOSE 53211
