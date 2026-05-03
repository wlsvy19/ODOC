# Codex Store

Spring Boot backend and Vue 3 frontend shopping mall sample.

Features:

- Fixed login accounts
- Product list, create, update, delete
- Cart quantity control
- Order checkout
- Order history
- Q&A board with admin answers
- H2 in-memory database with sample products

Accounts:

- Admin: `admin` / `admin`
- Users: `user1` / `user1` through `user10` / `user10`

## Backend

```powershell
cd backend
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`.

Useful endpoints:

- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `GET /api/orders`
- `GET /api/orders/{id}`
- `POST /api/orders`
- `GET /api/questions`
- `POST /api/questions`
- `PUT /api/questions/{id}`
- `PUT /api/questions/{id}/answer`
- `DELETE /api/questions/{id}`

H2 console is available at `http://localhost:8080/h2-console`.

## Frontend

```powershell
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`.
