# SCommerce

SCommerce es una aplicación de comercio electrónico que permite la creación, actualización y eliminación de cotizaciones y productos. La aplicación está construida con Spring Boot y utiliza PostgreSQL como base de datos.

## Requisitos

- Java 17 o superior
- Maven 3.6.3 o superior
- PostgreSQL
- Redis 

## Uso y configuracion

Comando para habilitar la terminal con inteligencia artificial(Warp)
```bash
command + t
```

Dar de baja a contenedor existente y generacion de nuevo(para actualizaciones) y puesta en marcha.
```bash
docker-compose down && docker-compose build && docker-compose up -d
```

Detener todos los contenedores, dar de baja el de Java, construirlo y volver a iniciar todos.
```bash
docker-compose down && docker-compose rm -f app && docker-compose build app && docker-compose up -d
```
Direccion para probar la api via Swagger.
http://localhost:8080/swagger-ui.html
