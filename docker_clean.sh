# Clean up the docker data
docker rm -f microblog_db microblog_backend
docker rmi microblog
docker network rm microblog_net
docker volume rm microblog_vol