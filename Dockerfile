# =============================================================================
# Stage 1 — Build
# Compiles the project and produces the executable JAR using Maven + JDK 21.
# The Maven wrapper is used so the pipeline never needs Maven pre-installed.
# =============================================================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy dependency manifests first — this layer is cached as long as pom.xml
# doesn't change, speeding up subsequent builds dramatically.
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Pre-fetch all dependencies into the local Maven cache.
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B --no-transfer-progress -q

# Copy the rest of the source tree and build (skip tests — tests run in CI).
COPY src/ src/
RUN ./mvnw package -B --no-transfer-progress -DskipTests -q

# =============================================================================
# Stage 2 — Runtime
# A minimal JRE 21 Alpine image; no build tools, no shell dependencies.
# Non-root user is created for security hardening.
# =============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Create a dedicated non-root user and group.
RUN addgroup -S numshield && adduser -S numshield -G numshield

WORKDIR /app

# Copy only the executable JAR from the build stage.
COPY --from=builder /build/target/*.jar app.jar

# Ensure the app directory is owned by the non-root user.
RUN chown -R numshield:numshield /app

USER numshield

# Expose the default Spring Boot port.
EXPOSE 8080

# JVM tuning: container-aware heap sizing; prefer IPv4.
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.net.preferIPv4Stack=true"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
