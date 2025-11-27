docker build --no-cache -t vladiin-app .
docker rm -f vladiin-container 2>/dev/null || true
docker run -d -p 8080:8080 -e API_BASE_URL=http://51.45.7.98 --name vladiin-container vladiin-app
echo Container running at http://localhost:8080