# Broken Rabbit - CyberSecurity CTF Training Platform

## Описание

**Broken Rabbit** — это учебный стенд для изучения веб-уязвимостей в формате CTF (Capture The Flag). Платформа представляет собой интерактивное приложение с анимированным кроликом, где участникам предстоит найти и использовать 10 различных уязвимостей.

Каждое успешно выполненное задание засчитывается глобально — достаточно одному участнику найти уязвимость, и она считается решённой для всех.

## Технологический стек

- **Backend:** Java 11, Spring Boot 2.7.5, PostgreSQL, Redis
- **Frontend:** React, Lottie анимация
- **Containerization:** Docker, Docker Compose

## Быстрый старт

### Требования
- Docker
- Docker Compose

### Установка и запуск

```bash
git clone https://github.com/your-repo/broken-rabbit.git
cd broken-rabbit
docker compose up -d
```
#### После запуска стенд будет доступен по адресу: http://localhost:3000

### Полный сброс стенда
```bash
docker compose down -v
docker volume rm broken-rabbit_postgres_data broken-rabbit_redis_data
docker compose build --no-cache
docker compose up -d
```
### Список уязвимостей

#	Уязвимость	Вес
1	Broken Access Control (IDOR)	2
2	SQL Injection	9
3	Stored XSS	4
4	Path Traversal	2
5	XML External Entity (XXE)	2
6	Server-Side Request Forgery (SSRF)	6
7	Insecure Deserialization	8
8	Log Injection (JNDI)	2
9	Race Condition	2
10	Prototype Pollution	3