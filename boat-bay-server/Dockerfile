FROM gcr.io/distroless/java-debian10:11

# Copy jar file to the container
COPY ./target/boat-bay-server-*.jar boat-bay-server.jar

# Execute the application
ENTRYPOINT ["java","-jar","boat-bay-server.jar"]
