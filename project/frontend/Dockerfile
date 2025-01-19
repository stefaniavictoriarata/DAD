# Use the Node.js image
FROM node:16

# Set the working directory
WORKDIR /usr/src/app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the app files
COPY . .

# Start the development server
CMD ["npm", "start"]

# Expose port
EXPOSE 3000
