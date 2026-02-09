# Activity Discovery Marketplace

A small full-stack demo project that lets you **discover activities**, **filter them**, **book them**, and **manage your bookings**. It uses a straightforward Spring Boot backend  and a clean Next.js UI suitable for demos.

---

## What this project does

### Activities
- Stores sample activities in an H2 database (seeded on startup)
- API endpoints:
  - `GET /activities` (optional filters: `city`, `category`, `maxPrice`)
  - `GET /activities/{id}`

### Bookings
- Adds a `Booking` entity linked to an `Activity`
- API endpoints:
  - `POST /bookings` (create booking)
  - `GET /bookings?userId=1` (list bookings for a user)

### Demo UI
- Next.js UI that:
  - Loads activities on page load
  - Has filters (city/category/maxPrice) with Search/Clear
  - Books an activity (POST request)
  - Loads a “My Bookings” list (GET request)

### Cancel booking
- Cancel bookings end-to-end:
  - Backend: `DELETE /bookings/{id}?userId=1`
  - Frontend: Cancel button in “My Bookings”

---

## Tech Stack

### Backend
- Java 17
- Spring Boot 3
- Spring Web (REST)
- Spring Data JPA + Hibernate
- H2 Database (local dev)

### Frontend
- Next.js (App Router)
- TypeScript
- CSS 

---

## Repo structure

```text
.
├─ activity-marketplace-backend/
│  ├─ pom.xml
│  └─ src/main/...
└─ activity-marketplace-frontend/
   ├─ package.json
   └─ src/app/...
```

---

## How it works (high-level)

- The backend seeds ~10 activities into H2 on startup (only if the table is empty).
- Bookings are stored as rows that link to an activity via a foreign key:

  `Booking -> Activity (ManyToOne)`

- The frontend calls the backend using a base URL from an environment variable:

  `NEXT_PUBLIC_API_URL`

- For now, there’s no authentication (by design). The UI uses:

  `userId = 1`

---

## Run locally

### 1) Backend (Spring Boot)
From the backend folder:
```bash
cd activity-marketplace-backend
mvn spring-boot:run
```

Backend will run on:
```text
http://localhost:8080
```

Quick check:
```text
http://localhost:8080/activities
```

### 2) Frontend (Next.js)
From the frontend folder:
```bash
cd activity-marketplace-frontend
npm install
```

Create `activity-marketplace-frontend/.env.local`:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Start the dev server:
```bash
npm run dev
```

Frontend will run on:
```text
http://localhost:3000
```

---

## API Endpoints

### Activities

**GET `/activities`**  
Optional query params:
- `city` (contains, case-insensitive)
- `category` (contains/easy match, case-insensitive)
- `maxPrice` (price <= `maxPrice`)

Examples:
```text
/activities?city=Lisbon
/activities?category=Food&maxPrice=50
```

**GET `/activities/{id}`**

---

### Bookings

**POST `/bookings`**  
Body:
```json
{ "userId": 1, "activityId": 3 }
```

**GET `/bookings?userId=1`**

**DELETE `/bookings/{id}?userId=1`**

---

## Test it locally (quick manual checklist)

### Browser tests (easy)

Activities JSON:
```text
http://localhost:8080/activities
```

Filter example:
```text
http://localhost:8080/activities?city=Lisbon&maxPrice=50
```

Bookings for user 1:
```text
http://localhost:8080/bookings?userId=1
```

### PowerShell tests (Windows-friendly)

Create a booking:
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/bookings" `
  -ContentType "application/json" `
  -Body '{"userId":1,"activityId":3}'
```

List bookings:
```powershell
Invoke-RestMethod "http://localhost:8080/bookings?userId=1"
```

Cancel booking (replace 1 with a real booking id):
```powershell
Invoke-RestMethod -Method Delete `
  -Uri "http://localhost:8080/bookings/1?userId=1"
```

---

## UI walkthrough

- Open `http://localhost:3000`
- Use filters (e.g., Lisbon / maxPrice 50) and click Search
- Click Clear
- Click Book on an activity → see a success message
- Click Load My Bookings
- Click Cancel on a booking → it disappears

---

## H2 Console (optional)

If enabled in your backend config:
```text
http://localhost:8080/h2-console
```

Use the JDBC URL shown in the backend logs (example):
```text
jdbc:h2:file:~/activity-marketplace-db
```

---

## Screenshots
- Home page*
- Filter results*
- Bookings list*

  (*Coming Soon)
---
## Deployment (coming soon)

Planned deployment so the app can be tested via a public link:
- Frontend on Vercel
- Backend hosted publicly (or via a simple cloud service)

(When deployed, this README will be updated with the live working url).
