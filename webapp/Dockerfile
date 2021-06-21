# npm run build:pi
# docker build -t pi1.wee:443/arm64v8/webit-webapp-pi:latest .
FROM node:15.13.0-alpine3.10 as build-step

RUN mkdir /app

WORKDIR /app

COPY package.json /app

RUN npm install

COPY . /app

RUN npm run build

# Stage 2
FROM arm64v8/nginx:1.19.9

COPY --from=build-step /app/build /usr/share/nginx/html

COPY default.conf /etc/nginx/conf.d/default.conf