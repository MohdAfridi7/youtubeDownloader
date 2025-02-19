# Step 1: Use a base image with Java 21
FROM eclipse-temurin:21-jdk

# Step 2: Set the working directory
WORKDIR /app

# Step 3: Copy the JAR file into the container
COPY target/playlist-download-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port that Spring Boot runs on
EXPOSE 8080

# Step 5: Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
