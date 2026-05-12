# Architect Task Management System

A full-stack task management system built for architecture firms to manage projects, assign tasks to junior architects, track daily work logs, upload artifacts, and monitor progress.

---

## Live Demo

| Service | URL |
|---|---|
| Frontend | https://inspiring-monstera-b962e0.netlify.app |
| Backend API | https://architect-task-system-production.up.railway.app/api |
| Swagger UI | https://architect-task-system-production.up.railway.app/swagger-ui.html |

### Demo Credentials

| Role | Email | Password |
|---|---|---|
| Manager | manager@example.com | password |
| Junior | junior@example.com | password |

> Register a new account via the login page if demo credentials are not set up.

---

## Features

### Manager
- Create and manage projects (name, client, location, project code)
- Assign tasks to junior architects with priority and due date
- Review and approve submitted work (status: Under Review → Approved / Revision Requested)
- Upload and download project artifacts (files)
- View all work logs submitted by team members
- Delete tasks and projects
- Manage user accounts (activate / deactivate)
- Dashboard with team-wide progress overview

### Junior Architect
- View assigned tasks
- Update task status (In Progress → Under Review)
- Submit daily work logs (what I did, planned for tomorrow, blockers, hours spent)
- Upload artifacts to tasks
- Add comments on tasks
- View personal dashboard with task summary

---

## Tech Stack

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| Angular | 18 | SPA framework |
| Angular Material | 18 | UI component library (MDC) |
| TypeScript | 5.x | Language |
| RxJS | 7.x | Reactive state & HTTP |
| Angular CDK | 18 | Overlay, portal for dialogs |

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Spring Boot | 3.4.5 | REST API framework |
| Spring Security | 6.x | JWT authentication & RBAC |
| Spring Data JPA | 3.x | ORM / database access |
| Hibernate | 6.x | JPA implementation |
| Java | 21 (LTS) | Language |
| Maven | 3.9 | Build tool |
| Lombok | latest | Boilerplate reduction |
| JJWT | 0.12.6 | JWT creation & validation |
| SpringDoc OpenAPI | 2.6.0 | Swagger UI / API docs |
| PostgreSQL Driver | latest | DB connectivity |

### Infrastructure & Services
| Service | Purpose | Free Tier |
|---|---|---|
| **Netlify** | Frontend hosting (CDN) | ✅ |
| **Railway** | Backend hosting (Java) | $5/month credit |
| **Supabase** | PostgreSQL database | ✅ 500MB |
| **Supabase Storage** | File artifact storage (S3-compatible) | ✅ 1GB |
| **GitHub** | Source control & CI/CD trigger | ✅ |

### CI/CD Flow
```
git push → GitHub
    ├── Netlify detects change → builds Angular → deploys to CDN
    └── Railway detects change → builds Spring Boot JAR → deploys container
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT (Browser)                      │
│              Angular 18 + Angular Material               │
└──────────────────────┬──────────────────────────────────┘
                       │ HTTPS REST API
                       ▼
┌─────────────────────────────────────────────────────────┐
│                 BACKEND (Railway)                        │
│              Spring Boot 3 / Java 21                     │
│  ┌──────────┐  ┌──────────┐  ┌───────────────────────┐  │
│  │Controllers│  │ Services │  │  Spring Security / JWT│  │
│  └──────────┘  └──────────┘  └───────────────────────┘  │
└──────────┬──────────────────────────┬───────────────────┘
           │ JPA / Hibernate           │ REST (S3-compatible)
           ▼                           ▼
┌──────────────────┐       ┌──────────────────────────────┐
│ Supabase         │       │ Supabase Storage              │
│ PostgreSQL DB    │       │ (Artifact file uploads)       │
└──────────────────┘       └──────────────────────────────┘
```

---

## Data Model

```
User
 ├── role: MANAGER | JUNIOR
 └── active: boolean

Project
 ├── name, description, clientName, location, projectCode
 ├── status: ACTIVE | COMPLETED | ON_HOLD
 └── tasks: Task[]

Task
 ├── title, description, priority: LOW|MEDIUM|HIGH|URGENT
 ├── status: ASSIGNED|IN_PROGRESS|UNDER_REVIEW|APPROVED|REVISION_REQUESTED
 ├── assignedTo: User, assignedBy: User
 ├── dueDate, estimatedHours
 ├── workLogs: WorkLog[]
 ├── artifacts: Artifact[]
 ├── comments: Comment[]
 └── statusHistory: TaskStatusHistory[]

WorkLog
 └── description, plannedForTomorrow, blockers, hoursSpent, logDate

Artifact
 └── fileName, fileUrl, storagePath, fileSize, version

Comment
 └── content, author, createdAt

TaskStatusHistory
 └── oldStatus, newStatus, remarks, changedBy, changedAt
```

---

## API Endpoints

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Login |
| POST | `/api/auth/register` | Public | Register |
| GET | `/api/projects` | All | List projects |
| POST | `/api/projects` | Manager | Create project |
| PUT | `/api/projects/{id}` | Manager | Update project |
| DELETE | `/api/projects/{id}` | Manager | Delete project |
| GET | `/api/tasks` | All | List tasks (filtered by role) |
| POST | `/api/tasks` | Manager | Assign task |
| PATCH | `/api/tasks/{id}/status` | All | Update task status |
| DELETE | `/api/tasks/{id}` | Manager | Delete task |
| POST | `/api/tasks/{id}/worklogs` | All | Submit work log |
| GET | `/api/tasks/{id}/worklogs` | All | Get work logs |
| POST | `/api/tasks/{id}/artifacts` | All | Upload file |
| GET | `/api/artifacts/{id}/download` | All | Download file |
| GET | `/api/notifications` | All | Get notifications |
| GET | `/api/dashboard` | All | Dashboard data |

Full API docs: `/swagger-ui.html`

---

## Local Development

### Prerequisites
- Java 21
- Maven 3.9+
- Node.js 18+
- Angular CLI 18

### Backend
```bash
cd backend

# Create local environment file
cp .env.example .env
# Fill in your values in .env

# Run
mvn spring-boot:run
# Runs on http://localhost:8080
```

### Frontend
```bash
cd frontend

npm install

# Run (proxies /api to localhost:8080)
ng serve
# Runs on http://localhost:4200
```

### Proxy Config
The frontend uses `src/proxy.conf.json` to forward `/api` calls to the backend during local development — no CORS issues locally.

---

## Environment Variables

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Base64 secret for signing JWT tokens |
| `JWT_EXPIRATION` | Token expiry in ms (default: 86400000 = 24h) |
| `SUPABASE_URL` | Supabase project URL |
| `SUPABASE_SERVICE_KEY` | Supabase service role key (for storage) |
| `SUPABASE_STORAGE_BUCKET` | Storage bucket name |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed frontend origins |
| `PORT` | Server port (Railway sets this automatically) |

---

## Security

- Passwords hashed with **BCrypt**
- Authentication via **JWT Bearer tokens** (stored in localStorage)
- Role-based access: `MANAGER` and `JUNIOR` roles enforced at endpoint level via `@PreAuthorize`
- CORS restricted to allowed origins only
- CSRF disabled (stateless JWT API)
- File uploads limited to **20MB**

---

## Deployment

### Backend — Railway
1. Connect GitHub repo → set Root Directory to `backend`
2. Add environment variables
3. Railway auto-detects `pom.xml` and builds with Maven

### Frontend — Netlify
1. Connect GitHub repo
2. `netlify.toml` at repo root handles build config automatically
3. Every `git push` triggers auto-redeploy

---

## Project Structure

```
architect-task-system/
├── backend/                        # Spring Boot API
│   ├── src/main/java/
│   │   └── com/architect/tasksystem/
│   │       ├── config/             # Security, CORS config
│   │       ├── controller/         # REST controllers
│   │       ├── dto/                # Request / Response DTOs
│   │       ├── entity/             # JPA entities
│   │       ├── enums/              # Role, Status enums
│   │       ├── exception/          # Global exception handler
│   │       ├── repository/         # Spring Data repositories
│   │       ├── security/           # JWT filter, SecurityUtils
│   │       ├── service/            # Business logic
│   │       └── storage/            # Supabase file storage
│   └── src/main/resources/
│       └── application.properties
├── frontend/                       # Angular app
│   ├── src/app/
│   │   ├── services/               # HTTP services
│   │   ├── models/                 # TypeScript interfaces
│   │   ├── interceptors/           # Auth JWT interceptor
│   │   ├── shared/                 # Layout, status badge, pipes
│   │   ├── dashboard/
│   │   ├── projects/
│   │   ├── tasks/
│   │   ├── worklogs/
│   │   ├── users/
│   │   └── notifications/
│   └── src/environments/
│       ├── environment.ts          # Local (uses proxy)
│       └── environment.prod.ts     # Production (Railway URL)
├── netlify.toml                    # Netlify build config
├── .gitignore
└── README.md
```
