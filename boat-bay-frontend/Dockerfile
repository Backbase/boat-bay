FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY /dist/boat-bay-frontend /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
