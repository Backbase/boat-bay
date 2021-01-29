build:
	#npm run build-prod
	docker build . --tag boat-bay:latest
	docker tag boat-bay:latest ${DOCKER_REPO}/boat-bay:latest
	docker push ${DOCKER_REPO}/boat-bay:latest
