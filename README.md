# NumShield API

A utility API for phone number intelligence, validation, and operator detection.

## Utilities

### Cameroon Phone Number Normalizer

The `CameroonPhoneNumberNormalizer` class provides a reusable utility method to validate and normalize Cameroon phone numbers to the standardized format: `+237XXXXXXXXX`.

#### Features
- Automatically removes formatting characters (spaces, hyphens, parentheses, and dots).
- Supports and normalizes various input formats:
  - Local 9-digit format (starting with `2` or `6`): `690123456` → `+237690123456`
  - Country code format without `+`: `237690123456` → `+237690123456`
  - Already normalized format: `+237690123456` → `+237690123456`
  - International exit code format: `00237690123456` → `+237690123456`
- Rejects invalid formats, wrong lengths, wrong country codes, invalid prefixes, or malformed inputs, throwing an `IllegalArgumentException` with a clear message.
- Safely handles `null`, empty, and blank inputs.

#### Usage Example

```java
import com.numshield.numshield_api.util.CameroonPhoneNumberNormalizer;

try {
    String normalized = CameroonPhoneNumberNormalizer.normalize("690 12 34 56");
    // Result: +237690123456
} catch (IllegalArgumentException e) {
    // Handle validation failure
    System.out.println("Validation failed: " + e.getMessage());
}
```

## REST API Endpoints & Swagger Documentation

A REST controller is exposed at `/api/v1/phone-numbers`. You can view the automatically generated Swagger UI and OpenAPI documentation when the application is running locally:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **OpenAPI YAML Spec**: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)
- **Static OAS3 YAML**: [`openapi/openapi.yaml`](openapi/openapi.yaml) — committed to the repo for offline reference and client SDK generation

### 1. Normalize Phone Number (GET)

**Request:**
```bash
curl "http://localhost:8080/api/v1/phone-numbers/normalize?number=690123456"
```

**Response (200 OK):**
```json
{
  "raw": "690123456",
  "normalized": "+237690123456"
}
```

### 2. Normalize Phone Number (POST)

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/phone-numbers/normalize \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "690 12 34 56"}'
```

**Response (200 OK):**
```json
{
  "raw": "690 12 34 56",
  "normalized": "+237690123456"
}
```

**Response on Error (400 Bad Request):**
```json
{
  "error": "Phone number contains invalid characters"
}
```

### 3. Validate Phone Number (GET)

**Request:**
```bash
curl "http://localhost:8080/api/v1/phone-numbers/validate?number=690123456"
```

**Response (200 OK):**
```json
{
  "phoneNumber": "+237690123456",
  "valid": true
}
```

### 4. Validate Phone Number (POST)

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/phone-numbers/validate \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "+237590123456"}'
```

**Response (400 Bad Request):**
```json
{
  "error": "Invalid Cameroon phone number prefix: must start with 2 or 6"
}
```

## Running the Application Locally

To start the local development server:

```bash
./mvnw spring-boot:run
```

## Running Tests

To run the unit and integration tests:

```bash
./mvnw clean test
```
