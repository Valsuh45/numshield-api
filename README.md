# NumShield API

NumShield is a Cameroon phone-number intelligence API. It normalizes numbers, validates them against the national mobile format, identifies the operator associated with a configured prefix allocation, and exposes those capabilities through a versioned REST API.

## Free initial release and v1 contract

The initial release is free, with no billing or paywall. Its scope is Cameroon mobile-number normalization, structural validation, and prefix-allocation lookup through the endpoints below.

`valid: true` means the number has a supported structural format. `operator` reports the configured prefix allocation, not a live lookup of the current serving network. Neither field proves that a number is allocated to a subscriber, reachable, active, or owned by the caller. NumShield does not send an OTP or verify ownership.

The wrapped response is the initial v1 contract (`1.0.0` in OpenAPI). The earlier unwrapped responses are a pre-release format; they are not served alongside this contract. Integrations already using that format must migrate before upgrading:

| Earlier response access | v1 response access |
| --- | --- |
| `raw`, `normalized` from normalization | `data.raw`, `data.normalized` |
| `phoneNumber`, `valid` from validation | `data.phoneNumber`, `data.valid` |
| String `error` and top-level `stage` | `error.message`, `error.stage`, and stable `error.code` |

Both outcomes include `success` and an ISO-8601 `timestamp`. Success includes `data`; failure includes `error`. Clients should regenerate SDKs from the updated OpenAPI contract, keep phone numbers as JSON strings, and use HTTP status plus `success` to distinguish outcomes. Future incompatible changes after this v1 release require a new major API path (for example `/api/v2`); optional additive fields may be introduced within v1.

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

POST requests require `Content-Type: application/json` and a non-blank JSON string: `{"phoneNumber":"..."}`. Numeric, boolean, array, object, null, and missing values are rejected with HTTP 400 and `INVALID_REQUEST` at `REQUEST_VALIDATION`; values are never coerced into strings.

Successful responses and the documented HTTP 400, 405, and 415 errors use the same envelope. Unsupported methods return 405 with an `Allow` header; unsupported request content types return 415. Both use `INVALID_REQUEST` at `REQUEST_VALIDATION`. Invalid phone-number strings return 400 with a descriptive error. Errors set `success` to `false` and include a stable code, message, and processing stage:

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
