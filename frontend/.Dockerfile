FROM node:alpine as build
WORKDIR /app
ARG REACT_APP_API_URL
ARG REACT_APP_MONGO_API_URL
ARG REACT_APP_CORS_URL
COPY package.json ./
COPY package-lock.json ./
COPY ./ ./
RUN npm ci
RUN npm run build
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
