FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY nginx.conf etc/nginx/nginx.conf
COPY mime.type etc/nginx/mime.type
COPY --from=builder /app/dist/PersonalFinanceAngular/browser/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
