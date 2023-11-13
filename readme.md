# HomeworkJustClick

## Projekt Inżynierski

To run PostgreSQL backend type:
```bash
  docker run -dp 5432:5432 --name homework_just_click_db -e POSTGRES_PASSWORD=123 -d postgres
```

To run MongoDB backend type:
```bash
  docker compose -f docker-compose.yaml up
```
or run file docker-compose.yaml

Swagger UI (POSTGRES) link:
```bash
  localhost:8080/api/swagger/ui.html
```