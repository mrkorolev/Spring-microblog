# Resources for containers
docker network create microblog_net
docker volume create microblog_vol

docker build -t microblog .

# PostgreSQL container with an db schema script to be run
docker run -d --name microblog_db --net=microblog_net \
    -e POSTGRES_DB=microblog \
    -e POSTGRES_USER=nikitakorolev \
    -e POSTGRES_PASSWORD=NikitaKorolev \
    -v $(pwd)/src/main/resources/schema.sql:/docker-entrypoint-initdb.d/schema.sql \
    -v microblog_vol:/var/lib/postgresql/data \
    postgres:14-alpine > /dev/null

# Application container, based on Tomcat (refer to Dockerfile)
docker run --rm -d --net=microblog_net --name microblog_backend \
    -e DB_HOST=microblog_db \
    -e DB_NAME=microblog \
    -e DB_PORT=5432 \
    -e USERNAME=nikitakorolev \
    -e PASSWORD=NikitaKorolev \
    -p 8080:8080 \
    microblog > /dev/null