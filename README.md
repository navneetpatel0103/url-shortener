# 🚀 Scalable URL Shortener

A high-performance, scalable URL shortening service built with **Spring Boot** and **React**. This project features a robust backend with caching and a modern, responsive frontend with smooth animations.

---

## ✨ Features

- **Quick Shortening**: Instantly convert long URLs into short, manageable links.
- **Fast Redirection**: High-speed redirection using Redis caching.
- **Modern UI**: Sleek, glassmorphic design with smooth transitions using Framer Motion.
- **Persistent Storage**: Robust data persistence with PostgreSQL.
- **Scalable Architecture**: Designed with system design best practices in mind.

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: PostgreSQL
- **Caching**: Redis
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS / Vanilla CSS
- **Animations**: Framer Motion
- **Icons**: Lucide React
- **HTTP Client**: Axios

---

## 📁 Project Structure

```text
url-shortner/
├── src/                    # Spring Boot Backend Source
│   ├── main/
│   │   ├── java/           # Java Source Code
│   │   └── resources/      # Configuration & Properties
├── url-shortener-ui/       # React Frontend (Vite)
│   ├── src/                # React Source Code
│   ├── public/             # Static Assets
│   └── package.json        # Frontend Dependencies
├── pom.xml                 # Maven Configuration
└── README.md               # Project Documentation
```

---

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- Node.js 18 or higher
- PostgreSQL
- Redis

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/url-shortner.git
   cd url-shortner
   ```
2. Configure `src/main/resources/application.properties` with your database and Redis credentials.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to the UI directory:
   ```bash
   cd url-shortener-ui
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

---

## 🔌 API Endpoints

### Shorten URL
- **Endpoint**: `POST /api/v1/shorten`
- **Request Body**:
  ```json
  {
    "url": "https://www.example.com/very-long-url-to-shorten"
  }
  ```
- **Response**:
  ```json
  {
    "shortKey": "abc123xy",
    "shortUrl": "http://localhost:8080/abc123xy"
  }
  ```

### Redirect
- **Endpoint**: `GET /{shortKey}`
- **Action**: Redirects to the original URL.

---

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.
