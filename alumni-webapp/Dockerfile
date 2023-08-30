# build environment
FROM node:16.14.0-alpine as build
ARG APP_VERSION
ENV REACT_APP_VERSION=$APP_VERSION
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json ./
COPY package-lock.json ./

RUN npm ci --silent
RUN npm install react-scripts@3.4.1 -g --silent
COPY . ./
RUN npm run build

# production environment
FROM nginx:stable-alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx /etc/nginx/conf.d

WORKDIR /usr/share/nginx/html
EXPOSE 80

CMD ["/bin/sh", "-c", "envsubst < env.placeholder.js > env.js && nginx -g \"daemon off;\""]
