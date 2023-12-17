# HomeworkJustClick

## Projekt Inżynierski
To run development environment using docker:
```bash
 docker-compose -f docker-compose.dev.yaml up
 ```
 or without logs:
 ```bash
 docker-compose -f docker-compose.dev.yaml up -d
 ```
When new changes are pulled, rebuild the images:
```bash
 docker-compose -f docker-compose.dev.yaml build
 ```
And then run docker-compose again:
```bash
 docker-compose -f docker-compose.dev.yaml up 
 ```
To run PostgreSQL backend type:
```bash
  docker run -dp 5432:5432 --name homework_just_click_db -e POSTGRES_PASSWORD=123 -d postgres
```

Swagger UI (POSTGRES) link:
```bash
  localhost:8080/api/swagger/ui.html
```