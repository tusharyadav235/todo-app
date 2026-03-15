# 🚀 Todo App — Java + Docker + GitHub Actions + EC2

> **Complete guide** — Local development se lekar EC2 production deployment tak

---

## 📁 Project Structure

```
todo-app/
├── backend/
│   ├── src/main/java/com/todo/
│   │   ├── TodoApplication.java       ← Main class
│   │   ├── controller/
│   │   │   └── TodoController.java    ← REST API endpoints
│   │   ├── model/
│   │   │   └── Todo.java              ← Database entity
│   │   ├── repository/
│   │   │   └── TodoRepository.java    ← DB operations
│   │   └── service/
│   │       └── TodoService.java       ← Business logic
│   ├── src/main/resources/
│   │   └── application.properties    ← Config
│   ├── Dockerfile                    ← Backend Docker image
│   └── pom.xml                       ← Maven dependencies
│
├── frontend/
│   ├── index.html                    ← Full UI (HTML + CSS + JS)
│   ├── Dockerfile                    ← Frontend Docker image (Nginx)
│   └── nginx.conf                    ← Nginx configuration
│
├── .github/
│   └── workflows/
│       └── deploy.yml                ← CI/CD Pipeline
│
├── docker-compose.yml                ← Dono services ek saath
└── README.md                         ← Yahi file
```

---

## 🔗 API Endpoints

| Method | URL | Kya karta hai |
|--------|-----|---------------|
| GET | `/api/todos` | Saare todos laao |
| GET | `/api/todos/{id}` | Ek todo laao |
| POST | `/api/todos` | Naya todo banao |
| PUT | `/api/todos/{id}` | Todo update karo |
| PATCH | `/api/todos/{id}/toggle` | Complete/incomplete toggle |
| DELETE | `/api/todos/{id}` | Todo delete karo |
| GET | `/api/todos/health` | Health check |

---

## 💻 Local Development (Step by Step)

### Step 1: Prerequisites install karo
```bash
# Java 17 check karo
java -version

# Maven check karo
mvn -version

# Docker check karo
docker --version
docker compose version
```

### Step 2: Backend locally chalao
```bash
cd backend
mvn spring-boot:run
# Backend http://localhost:8080 par chalega
```

### Step 3: Frontend open karo
```bash
# Seedha browser mein open karo:
# frontend/index.html file ko browser mein drag karo
# Ya VS Code Live Server use karo
```

### Step 4: Docker se chalao (recommended)
```bash
# Project root mein jao
cd todo-app

# Dono services build aur start karo
docker compose up --build

# Background mein chalao
docker compose up -d --build

# Logs dekho
docker compose logs -f

# Band karo
docker compose down
```

---

## ☁️ EC2 Deployment Guide

### Step 1: EC2 Instance Setup

1. AWS Console mein jao → EC2 → Launch Instance
2. **AMI**: Ubuntu Server 22.04 LTS
3. **Instance Type**: t2.micro (free tier)
4. **Security Group** — Yeh ports kholo:
   - Port 22 (SSH) — Sirf apne IP se
   - Port 80 (HTTP) — Anywhere (0.0.0.0/0)
   - Port 8080 (Backend API) — Anywhere

5. **Key Pair** banao aur .pem file download karo

### Step 2: EC2 par Docker install karo
```bash
# SSH se connect karo
ssh -i your-key.pem ubuntu@YOUR_EC2_IP

# System update karo
sudo apt update && sudo apt upgrade -y

# Docker install karo
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Ubuntu user ko docker group mein add karo (sudo ke bina docker chalane ke liye)
sudo usermod -aG docker ubuntu

# Logout aur login karo (group apply hone ke liye)
exit
ssh -i your-key.pem ubuntu@YOUR_EC2_IP

# Docker Compose install karo
sudo apt install docker-compose-plugin -y

# Check karo
docker --version
docker compose version
```

### Step 3: GitHub Secrets set karo

GitHub repo mein jao → Settings → Secrets and variables → Actions → New repository secret

| Secret Name | Value |
|-------------|-------|
| `DOCKER_USERNAME` | Tumhara Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub password ya Access Token |
| `EC2_HOST` | EC2 ka Public IP ya DNS |
| `EC2_SSH_KEY` | .pem file ka poora content (-----BEGIN... se -----END... tak) |

### Step 4: Docker Hub par account banao

1. hub.docker.com par jao
2. Account banao
3. Settings → Security → New Access Token banao
4. Isko `DOCKER_PASSWORD` secret mein daalo

### Step 5: Frontend mein EC2 IP update karo

`frontend/index.html` file mein yeh line dhundho:
```javascript
const API_BASE = 'http://localhost:8080/api/todos';
```

Isko apne EC2 IP se badlo:
```javascript
const API_BASE = 'http://YOUR_EC2_IP:8080/api/todos';
```

### Step 6: Deploy karo!

```bash
# Code commit karo
git add .
git commit -m "Initial todo app"
git push origin main

# GitHub Actions automatically:
# 1. Backend build & test karega
# 2. Docker images banayega
# 3. Docker Hub par push karega
# 4. EC2 par deploy karega
```

---

## 🔍 Deployment Check karo

```bash
# EC2 par SSH karo
ssh -i your-key.pem ubuntu@YOUR_EC2_IP

# Containers dekho
docker ps

# Logs dekho
docker compose logs -f backend
docker compose logs -f frontend

# Health check
curl http://localhost:8080/api/todos/health

# Todo banao (test)
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Todo","description":"Yeh kaam karna hai"}'
```

Browser mein open karo:
- **Frontend**: `http://YOUR_EC2_IP`
- **Backend API**: `http://YOUR_EC2_IP:8080/api/todos`
- **H2 Console**: `http://YOUR_EC2_IP:8080/h2-console`

---

## 🛠️ Common Problems & Solutions

### Problem: Port already in use
```bash
sudo lsof -i :8080
sudo kill -9 PID
```

### Problem: Docker permission denied
```bash
sudo usermod -aG docker $USER
newgrp docker
```

### Problem: EC2 se connect nahi ho raha
- Security Group mein port khula hai? Check karo
- Instance running hai? AWS Console mein dekho

### Problem: GitHub Actions fail ho raha hai
- Saare 4 secrets sahi se set hain? Double check karo
- Docker Hub credentials sahi hain?
- EC2_SSH_KEY mein poora content daala? (newlines sahi hone chahiye)

---

## 📚 Technologies Used

| Technology | Version | Use |
|------------|---------|-----|
| Java | 17 | Backend language |
| Spring Boot | 3.2 | REST API framework |
| H2 Database | Latest | In-memory DB |
| Maven | 3.9 | Build tool |
| Docker | Latest | Containerization |
| Nginx | Alpine | Frontend server |
| GitHub Actions | Latest | CI/CD |
| AWS EC2 | Ubuntu 22.04 | Cloud hosting |

---

## 🎯 Next Steps (Improvements)

- [ ] MySQL/PostgreSQL add karo (H2 replace karo)
- [ ] JWT Authentication add karo
- [ ] HTTPS setup karo (Let's Encrypt)
- [ ] Custom domain add karo
- [ ] Redis caching add karo
- [ ] Monitoring setup karo (Prometheus + Grafana)

---

*Banaya gaya ❤️ ke saath — Happy Coding!*
