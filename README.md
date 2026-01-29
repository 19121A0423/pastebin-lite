# Pastebin Lite

A simple Pastebin-like application built using **Spring Boot**, **Redis**, and **Angular**.


## Deployed URLs

- **Backend API**  
  https://pastebin-lite-agtr.onrender.com

 **Available APIs**
- `GET /api/healthz`
- `POST /api/pastes`
- `GET /api/pastes/{id}`
- `GET /p/{id}` (HTML view)


- **Frontend**  
  https://pastebin-litef.onrender.com


## 📦 Git Repository

https://github.com/19121A0423/pastebin-lite


## ▶️ Run Locally

### Backend (Spring Boot)
```bash
cd pastebin
mvn spring-boot:run

## Backend runs at
http://localhost:8000/api

## Frontend (Angular)
cd pastebin-workspace
npm install
ng serve

## Frontend runs at
http://localhost:4200
