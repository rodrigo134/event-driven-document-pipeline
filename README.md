# Event-Driven Document Ingestion Pipeline

A concise, production-focused example demonstrating an event-driven, exactly-once document ingestion pipeline built with Spring Boot. The project shows how to ingest files into object storage (MinIO), deduplicate uploads by content hash, persist metadata in Postgres, and process documents through resilient, event-driven components.

What this repository demonstrates
- Content-based deduplication at ingestion (SHA-based file hash).
- Exactly-once semantics using idempotency keys and correlation IDs.
- Saga-style compensation for long-running workflows and partial failures.
- Resiliency patterns: retries, circuit breakers and Dead Letter Queue for failed items.

High-level structure
- src/main/java: Spring Boot application and services
  - document/: domain objects, repository, services for file metadata and workflow state
  - storage/: MinIO client, upload controller and storage integration
  - config/: MinIO and infrastructure configuration
- docker-compose.yml: development stack (MinIO, Postgres, optionally LocalStack)
- .env.example: template containing the minimal environment variables required (safe to commit)

Security and secrets
- Do NOT commit real secrets or credentials. Keep any real passwords, access keys, or tokens out of the repository.
- This project tracks `application.properties` intentionally for example purposes. Only example-safe values should be present in tracked config files.
- A `.env.example` file is provided as a template with the minimal placeholders required to run locally. Copy it to `.env` and fill values for local development only. Do not commit `.env` with real credentials.

Quickstart (local development)
Prerequisites: Java 17, Docker & docker-compose, Maven (project includes the Maven Wrapper)

1) Prepare local environment
   - Copy the template and fill values for local testing:

     Windows (cmd.exe):
     copy .env.example .env

   - Edit `.env` and provide values only for the placeholders shown in `.env.example`.

2) Start infrastructure
   - docker-compose up -d minio postgres

3) Run the application
   - Windows (cmd.exe):
     .\mvnw.cmd spring-boot:run

4) Upload and test
   - Use the REST endpoints exposed by the application (see `src/main/java/.../storage/FileUploadController.java`) to upload files and observe the ingestion pipeline behavior.

Notes on environment variables
- `.env.example` intentionally contains only the minimal placeholders required for this project (see the file for exact names). It is intended as a template only.
- Keep `.env` local and out of version control.

Diagnostics and testing
- The project contains benchmark scripts and test scenarios used during development (k6 + LocalStack). Those are for development and load testing only.

How to handle accidental secret commits
1. Remove the file and commit: git rm --cached .env && git commit -m "remove .env from repo"
2. If secrets were committed to history, rewrite history using `git filter-repo` or the BFG Repo-Cleaner and rotate the compromised credentials.

Contributing
- Please open issues or PRs for improvements. Keep changes focused and include tests where applicable.

Contact
- Open an issue on this repository with questions or suggestions.
