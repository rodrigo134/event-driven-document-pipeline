# Event-Driven Document Pipeline

Spring Boot API for uploading documents, storing the original file in MinIO, saving metadata in PostgreSQL, and processing the upload asynchronously through RabbitMQ.

## Current Flow

1. `POST /files/upload` receives a multipart file.
2. The application calculates a SHA-256 hash from the file content.
3. If another document with the same hash already exists, the upload is rejected.
4. The file is saved in MinIO using a generated object key.
5. A `documents` row is created in PostgreSQL with status `UPLOADED`.
6. A `DocumentUploadedEvent` is published to RabbitMQ.
7. The consumer reads the event, loads the file from MinIO, and updates the document status:
   - `PROCESSING` while the worker is running
   - `PROCESSED` when the file can be read successfully
   - `FAILED` when processing throws an exception

The processing step is intentionally simple right now: it validates that the worker can access and read the stored object. It does not parse document contents yet.

## Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring AMQP
- PostgreSQL
- RabbitMQ
- MinIO
- Maven Wrapper

## Project Structure

```text
src/main/java/com/rodrigo134/event_driven_document_pipeline
├── config/       Infrastructure configuration
├── document/     Document entity, repository, status, and upload service
├── exception/    Domain exceptions
├── ingestion/    Upload HTTP controller
├── processing/   Asynchronous document processor
├── queue/        RabbitMQ event publisher, consumer, and event payload
└── storage/      MinIO file storage service
```

## Local Setup

### 1. Configure environment variables

Copy the example file:

```powershell
Copy-Item .env.example .env
```

Fill in the values in `.env`.

### 2. Start dependencies

```powershell
docker compose up -d
```

Services:

- PostgreSQL: `localhost:5432`
- RabbitMQ: `localhost:5672`
- RabbitMQ Management UI: `http://localhost:15672`
- MinIO API: `http://localhost:9000`
- MinIO Console: `http://localhost:9001`

Create a MinIO bucket named `documents` before uploading files.

### 3. Run the application

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts on the default Spring Boot port: `http://localhost:8080`.

## API

### Upload a document

```http
POST /files/upload
Content-Type: multipart/form-data
```

Form field:

- `file`: document file

Example with curl:

```bash
curl -F "file=@example.pdf" http://localhost:8080/files/upload
```

### Get document metadata

```http
GET /documents/{id}
```

Returns the document metadata, including object key, hash, upload time, processing status, processed time, and failure reason when applicable.

## Notes

- Deduplication is based on the SHA-256 hash stored in PostgreSQL.
- RabbitMQ is used for asynchronous handoff after the upload record is created.
- There is no dead-letter queue, retry policy, circuit breaker, saga, or exactly-once guarantee implemented in the current codebase.
- Keep real credentials out of Git. Use `.env` locally and keep `.env.example` as the committed template.
