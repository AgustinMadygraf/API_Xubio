## 🚀 Pasos para Construir y Ejecutar con Docker
Construir la imagen

```sh
docker compose -f 'docker\docker-compose.yml' up -d --build 
```

Ejecutar el contenedor
```sh
docker run -p 8080:8080 api_xubio
```
