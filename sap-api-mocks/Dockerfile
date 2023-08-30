FROM node:16-alpine

# Create app directory
WORKDIR /app

COPY package*.json ./
RUN npm ci


# Bundle app source
COPY . ./

EXPOSE 3100
CMD [ "npm", "run", "mocks" ]
