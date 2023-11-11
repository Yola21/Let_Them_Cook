# Use an official OpenJDK runtime as a parent image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory to /app
WORKDIR /app

RUN ls

# Copy only the necessary files to cache dependencies and build the project
# COPY Backend/letscook/letscook/build.gradle Backend/letscook/letscook/settings.gradle Backend/letscook/letscook/gradlew Backend/letscook/letscook/gradlew.bat /app/

# COPY Backend/letscook/letscook/gradle /app/gradle
COPY . .

RUN chmod 777 Backend/letscook/letscook/gradlew

# Download dependencies
RUN Backend/letscook/letscook/gradlew --version

# Copy the entire project


# Build the project
RUN Backend/letscook/letscook/gradlew build

# Expose port 8080 (adjust as needed)
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "Backend/letscook/letscook/build/libs/letscook-0.0.1-SNAPSHOT.jar"]
