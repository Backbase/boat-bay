build:
	rm -rf dist
	npm run build
	docker build . --tag boat-bay:latest
	docker tag boat-bay:latest ${DOCKER_REPO}/boat-bay:latest
	docker push ${DOCKER_REPO}/boat-bay:latest

apply: build
	kubectl apply -f kubernetes-manifests/
	kubectl rollout restart deployment/boat-bay-ui

