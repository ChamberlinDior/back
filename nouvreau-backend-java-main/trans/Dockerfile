# Step 1: Build the application
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /opt/app

# Set UTF-8 encoding
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Copy Maven wrapper and related files
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Download dependencies offline
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Package the application without running tests
RUN ./mvnw clean package -DskipTests

# Step 2: Run the application in a minimal image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app

# Copy the JAR from the build stage
COPY --from=build /opt/app/target/*.jar /opt/app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
