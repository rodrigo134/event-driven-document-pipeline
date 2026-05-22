┌──


| Cenário                          | Resultado comum                    | Resultado que você resolve                                         |
| -------------------------------- | ---------------------------------- | ------------------------------------------------------------------ |
| Uploada 50 documentos            | 3 falham no meio, some do sistema  | Cada um é rastreado, falha é detectada, reprocessa automaticamente |
| Uploada o mesmo PDF 2x           | Processa 2x, gera duplicatas       | Detecta por hash, ignora o segundo                                 |
| Lambda morre no meio do chunking | Documento fica órfão, ninguém sabe | Saga compensa: limpa o que fez, notifica, marca como falha         |
| API de embedding cai             | Pipeline trava, fila explode       | Circuit breaker abre, vai pra fila de retry com backoff            |





## Pipeline de Ingestão de Documentos — Exactly-Once Serverless

### Problema
RAG pipelines falham silenciosamente: documento some no meio do processamento,
duplicatas poluem o índice, falhas só são descobertas dias depois.

### Solução
Pipeline event-driven (S3 → EventBridge → SQS → Lambda) com:
- Deduplicação por hash de conteúdo no ingresso
- Idempotência por correlation ID + step
- Saga pattern com compensação automática
- Circuit breaker por provedor de embedding

### Arquitetura
[diagrama]

### Métricas (benchmark com k6 + LocalStack)
| Cenário | Taxa de sucesso | Tempo médio | Observação |
|---------|----------------|-------------|------------|
| 10k docs normais | 99.97% | 2.3s | 3 falhas, todas compensadas |
| 1k duplicatas | 100% (1 processado) | 0.8s | 999 ignorados |
| Kill Lambda no meio | 100% | 4.1s | Compensação em 1.2s |
| API embedding cai | 99.94% | 8.7s | Fallback pra retry, depois DLQ |

### Como rodar
```bash
docker-compose up -d localstack postgres
./gradlew bootRun
k6 run benchmark/upload-normal.js
k6 run benchmark/upload-duplicates.js
k6 run benchmark/kill-lambda.js  # chaos test