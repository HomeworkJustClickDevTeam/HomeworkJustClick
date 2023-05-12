# HomeworkJustClick

## Projekt Inżynierski

To run PostgreSQL backend type:
```bash
  docker run -dp 5432:5432 --name homework_just_click -e POSTGRES_PASSWORD=123 -d postgres
```

To run MongoDB backend type:
```bash
  docker compose -f docker-compose.yaml up
```
or run file docker-compose.yaml

Swagger UI link:
```bash
  localhost:8080/swagger/ui.html
```