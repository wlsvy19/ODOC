# Codex Board Test

Spring Boot backend and Vue 3 frontend board CRUD sample.

## Backend

```powershell
cd backend
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`.

Useful endpoints:

- `GET /api/posts`
- `GET /api/posts/{id}`
- `POST /api/posts`
- `PUT /api/posts/{id}`
- `DELETE /api/posts/{id}`

H2 console is available at `http://localhost:8080/h2-console`.

## Frontend

```powershell
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`.

