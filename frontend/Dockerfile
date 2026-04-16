# Stage 1: Build React app
FROM node:18-alpine AS builder
WORKDIR /app

# Копируем manifest-файлы первыми для кеширования
COPY package.json package-lock.json* ./
RUN npm install --legacy-peer-deps

# Копируем исходный код
COPY . .

# Переменные окружения для React
ENV REACT_APP_API_URL=/api

# Собираем проект
RUN npm run build

# Stage 2: Production nginx server
FROM nginx:alpine

# Копируем собранный React-билд
COPY --from=builder /app/build /usr/share/nginx/html

# Копируем конфиг nginx
COPY <<'EOF' /etc/nginx/conf.d/default.conf
types {
    application/octet-stream lottie;
}

server {
    listen 80;
    server_name _;
    
    add_header Cross-Origin-Opener-Policy same-origin;
    add_header Cross-Origin-Embedder-Policy require-corp;
    
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api/ {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Cid $http_x_cid;
        proxy_set_header Authorization $http_authorization;
        proxy_connect_timeout 10s;
        proxy_read_timeout 30s;
        proxy_next_upstream error timeout http_502 http_503 http_504;
        proxy_next_upstream_tries 2;
        proxy_buffering off;
    }
    
    location /ws/ {
        proxy_pass http://backend:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Cid $http_x_cid;
    }
}
EOF

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]