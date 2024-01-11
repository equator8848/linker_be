docker build -t equator-linker:latest .

docker rm -vf linker-instance

docker run -d -p 8888:8888 --restart always --name linker-instance equator-linker:latest

docker logs -f linker-instance
