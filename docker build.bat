docker build -t vladiin-app .
docker run -d -p 8080:8080 --name vladiin-container vladiin-app