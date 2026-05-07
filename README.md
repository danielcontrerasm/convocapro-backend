# ConvocaPro Backend Fixed

Este backend sí es un proyecto Spring Boot completo y runnable.

## Incluye
- Spring Boot 3.3.5
- Java 17
- PostgreSQL y H2
- Auth login/register
- JWT básico
- Admin de usuarios
- Retries por usuario
- Total de accesos
- Perfiles: DEMO, ASISTENCIAL, TECNICO, PROFESIONAL, FULL
- Cursos por perfil
- Actividades por unidad
- Seed automático:
  - 10 DEMO
  - 200 ASISTENCIAL
  - 200 TECNICO
  - 200 PROFESIONAL
- Examen final de 200 preguntas por usuario/perfil
- Hoja de resultados con review

## Correr rápido con H2
```bash
mvn spring-boot:run
```

Backend:
```text
http://localhost:8080
```

Health:
```text
http://localhost:8080/api/health
```

## Correr con PostgreSQL + Docker
```bash
docker compose down -v
docker compose up --build
```

## Usuarios demo
```text
admin / 1234
tecnico / 1234
```

## Endpoints principales

### Auth
```http
POST /api/auth/login
POST /api/auth/register
POST /api/auth/logout
```

Login body:
```json
{
  "username": "admin",
  "password": "1234"
}
```

Logout requires the current bearer token:
```http
Authorization: Bearer <token>
```

### Usuarios/admin
```http
GET /api/users
GET /api/users/stats
GET /api/users/{id}
PUT /api/users/{id}/retries
```

### Cursos
```http
GET /api/courses?profile=TECNICO
```

### Exámenes
```http
GET /api/exams/demo?count=10
GET /api/exams/full?userId=1
POST /api/exams/submit
```


## Exam Type Support Added

Endpoint now supports:

```http
GET /api/exams/full?userId=1&examType=GENERIC
GET /api/exams/full?userId=1&examType=TERRITORIAL_12
```

If you already had data before this update, run:

```sql
ALTER TABLE questions ADD COLUMN IF NOT EXISTS exam_type VARCHAR(50);
UPDATE questions SET exam_type = 'GENERIC' WHERE exam_type IS NULL;
```

Then import the Territorial 12 SQL script.
