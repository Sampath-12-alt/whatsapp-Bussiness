# whatsapp-business-platform

Spring Boot foundation for the WhatsApp-first Commerce & Business Management Platform.

## Requirements

- Java 17
- Maven 3.9+

## Local MVP

Start PostgreSQL and the application with the development-only security profile:

```bash
docker compose up -d postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The `dev` profile allows unauthenticated access only to the Product API and disables CSRF for local API testing. Do not activate it in production. Authentication and authorization are not implemented yet: `businessId` selects/scopes product records but does not prove that a caller is authorized for that business. Without the `dev` profile, Spring Security's default authentication requirements remain active.

Select an existing business ID. If the database is empty, the commands below create a local test business and use the ID returned by PostgreSQL:

```bash
BUSINESS_ID=$(docker compose exec -T postgres psql -U platform -d whatsapp_business -qAt -c "SELECT id FROM businesses ORDER BY id LIMIT 1;")
if [ -z "$BUSINESS_ID" ]; then
	BUSINESS_ID=$(docker compose exec -T postgres psql -U platform -d whatsapp_business -qAt -c "INSERT INTO businesses (name) VALUES ('MVP test business') RETURNING id;")
fi
printf 'Using business ID %s\n' "$BUSINESS_ID"
```

Create and list a product:

```bash
curl -i -X POST "http://localhost:8080/api/businesses/${BUSINESS_ID}/products" \
	-H 'Content-Type: application/json' \
	-d '{"name":"Sample product","description":"MVP test","price":12.50,"unit":"piece","available":true}'

curl -i "http://localhost:8080/api/businesses/${BUSINESS_ID}/products"
```

## Build

```bash
mvn clean package
```

## Run (without development access)

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/whatsapp-business-platform-0.0.1-SNAPSHOT.jar
```

The project currently contains only the application foundation. Business modules, database credentials, migrations, and authentication logic will be added separately.
