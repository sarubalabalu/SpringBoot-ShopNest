FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 10000

CMD ["sh", "-c", "java -jar target/ecommerce-1.0.0.jar --server.port=${PORT:-10000}"]
