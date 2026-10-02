# NumShield API

NumShield is a Cameroon phone-number intelligence API. It normalizes numbers, validates them against the national mobile format, identifies the originating telecom operator, and exposes those capabilities through a versioned REST API.

## Requirements

- Java 21
- Docker (optional)

## Run locally

```bash
./mvnw spring-boot:run
```

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`; generated OpenAPI JSON is available at `http://localhost:8080/v3/api-docs`.

## Verify a phone number

`POST /api/v1/number/verify` runs normalization, validation, and operator detection in one request.

```bash
curl -X POST http://localhost:8080/api/v1/number/verify \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber":"690 12 34 56"}'
```

```json
{
  "success": true,
  "data": {
    "input": "690 12 34 56",
    "normalized": "+237690123456",
    "valid": true,
    "operator": "ORANGE",
    "countryCode": "237",
    "country": "CM"
  },
  "timestamp": "2026-10-02T08:00:00Z"
}
```

Known operators are `MTN`, `ORANGE`, `NEXTTEL`, and `CAMTEL`. A structurally valid mobile range that has no configured allocation is returned as `UNKNOWN`.

## Other endpoints

- `GET /api/v1/phone-numbers/normalize?number=690123456`
- `POST /api/v1/phone-numbers/normalize`
- `GET /api/v1/phone-numbers/validate?number=690123456`
- `POST /api/v1/phone-numbers/validate`

POST requests accept `{"phoneNumber":"..."}`. Every endpoint uses the same response envelope. Errors set `success` to `false` and include a stable code, message, and processing stage:

```json
{
  "success": false,
  "error": {
    "code": "INVALID_PHONE_NUMBER",
    "message": "Phone number contains invalid characters",
    "stage": "NORMALIZATION"
  },
  "timestamp": "2026-10-02T08:00:00Z"
}
```

## Test and package

```bash
./mvnw test
./mvnw package
docker build -t numshield-api .
```

The maintained static API contract is in [`openapi/openapi.yaml`](openapi/openapi.yaml).
