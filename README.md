# SWAPI Backend API

API REST que proporciona acceso a datos de Star Wars desde SWAPI (Star Wars API) con autenticacion JWT.


<div align="center">

[![Heroku](https://img.shields.io/badge/Heroku-Deployed-430098?style=for-the-badge&logo=heroku&logoColor=white)](https://heroku.com)

### 🚀 Enlaces de Produccion

[![Backend API](https://img.shields.io/badge/Backend_API-Live-FF6B6B?style=for-the-badge&logo=spring&logoColor=white)](https://swapi-backend-555a6e710c16.herokuapp.com/)
[![Swagger Docs](https://img.shields.io/badge/Swagger-Documentation-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swapi-backend-555a6e710c16.herokuapp.com/swagger-ui/index.html)

</div>

### 📋 Servicios Desplegados

| 🎨 Servicio | 🔗 Enlace | 📝 Descripcion |
|------------|-----------|---------------|
| **Backend API** | [swapi-backend-555a6e710c16.herokuapp.com](https://swapi-backend-555a6e710c16.herokuapp.com/) | API REST con Spring Boot y PostgreSQL |
| **Swagger Docs** | [swagger-ui/index.html](https://swapi-backend-555a6e710c16.herokuapp.com/swagger-ui/index.html) | Documentacion interactiva de la API |
| **OpenAPI JSON** | [v1/api-docs](https://swapi-backend-555a6e710c16.herokuapp.com/v1/api-docs) | Especificacion OpenAPI en JSON |

## 🏗️ Estructura del Proyecto

```
src/main/java/com/xavi/swapi/
├── 📁 config/          # Configuraciones (Security, OpenAPI, WebClient)
├── 📁 controller/      # Controladores REST
├── 📁 dto/             # Data Transfer Objects
│   └── 📁 swapi/       # DTOs para respuestas de SWAPI
├── 📁 entity/          # Entidades JPA
├── 📁 exception/       # Manejo de excepciones
├── 📁 repository/      # Repositorios JPA
├── 📁 security/        # Filtros y servicios JWT
├── 📁 service/         # Logica de negocio
└── 📁 client/          # Cliente HTTP para SWAPI externa
```

## 🚀 Inicio Rapido

### Desarrollo Local (H2)

```bash
./mvnw spring-boot:run
```

## 🛠️ Stack Tecnologico

| Tecnologia | Descripcion |
|------------|-------------|
| ![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white) | Lenguaje de programacion |
| ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=flat&logo=springboot&logoColor=white) | Framework backend |
| ![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=flat&logo=springsecurity&logoColor=white) | Autenticacion y autorizacion |
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat&logo=postgresql&logoColor=white) | Base de datos produccion |
| ![H2](https://img.shields.io/badge/H2-Database-0000BB?style=flat&logo=databricks&logoColor=white) | Base de datos desarrollo |
| ![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat&logo=swagger&logoColor=black) | Documentacion API |
| ![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=flat&logo=docker&logoColor=white) | Containerizacion |

## 📡 Endpoints de la API

### 🔐 Autenticacion

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Registrar nuevo usuario |
| `POST` | `/api/auth/login` | Iniciar sesion |
| `POST` | `/api/auth/refresh` | Renovar access token |

### 🌟 Recursos de Star Wars

> ⚠️ Requieren autenticacion JWT

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/api/people` | Listar personajes |
| `GET` | `/api/people/{id}` | Detalle de personaje |
| `GET` | `/api/films` | Listar peliculas |
| `GET` | `/api/films/{id}` | Detalle de pelicula |
| `GET` | `/api/starships` | Listar naves estelares |
| `GET` | `/api/starships/{id}` | Detalle de nave estelar |
| `GET` | `/api/vehicles` | Listar vehiculos |
| `GET` | `/api/vehicles/{id}` | Detalle de vehiculo |

### Favoritos

> Requieren autenticacion JWT

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/favorites` | Agregar a favoritos |
| `GET` | `/api/favorites` | Listar favoritos del usuario |
| `GET` | `/api/favorites/check` | Verificar si recurso es favorito |
| `DELETE` | `/api/favorites/{id}` | Eliminar de favoritos |

#### Ejemplo de uso de Favoritos

**Agregar favorito:**
```json
POST /api/favorites
{
  "resourceType": "PEOPLE",
  "resourceId": "1"
}
```

**Tipos de recursos:** `PEOPLE`, `FILMS`, `STARSHIPS`, `VEHICLES`

**Listar favoritos (incluye detalle del recurso):**
```
GET /api/favorites?type=PEOPLE
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "resourceType": "PEOPLE",
    "resourceId": "1",
    "createdAt": "2026-01-14T13:30:24",
    "resource": {
      "uid": "1",
      "name": "Luke Skywalker",
      "gender": "male",
      "height": "172",
      ...
    }
  }
]
```

### 🔍 Parametros de Consulta

| Parametro | Tipo | Descripcion |
|-----------|------|-------------|
| `page` | number | Numero de pagina (default: 1) |
| `limit` | number | Elementos por pagina (default: 10) |
| `name` | string | Filtrar por nombre |

## 🔑 Autenticacion JWT


### Ejemplo de Uso

**1. Registro**
```json
POST /api/auth/register
{
  "email": "usuario@ejemplo.com",
  "password": "Password1!"
}
```

**2. Login**
```json
POST /api/auth/login
{
  "email": "usuario@ejemplo.com",
  "password": "Password1!"
}
```

**3. Respuesta**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**4. Usar Token**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## ⚙️ Configuracion

### Variables de Entorno

| Variable | Descripcion |
|----------|-------------|
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`dev` / `prod`) |
| `DB_URL` | URL de conexion PostgreSQL |
| `DB_USERNAME` | Usuario de base de datos |
| `DB_PASSWORD` | Contrasena de base de datos |
| `JWT_SECRET` | Clave secreta para JWT |

### Perfiles

| Perfil | Base de Datos | Uso |
|--------|---------------|-----|
| `dev` | H2 (memoria) | Desarrollo local |
| `prod` | PostgreSQL | Produccion |

## 🧪 Pruebas

### Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar con detalles
./mvnw test -Dtest=*Test

# Ejecutar una prueba especifica
./mvnw test -Dtest=AuthControllerTest
```

### Estructura de Pruebas

```
src/test/java/com/xavi/swapi/
├── controller/     # Pruebas de integracion de controladores
├── service/        # Pruebas unitarias de servicios
└── security/       # Pruebas de seguridad y JWT
```

### Tecnologias de Testing

| Herramienta | Uso |
|-------------|-----|
| **JUnit 5** | Framework de pruebas |
| **MockMvc** | Pruebas de controladores REST |
| **WireMock** | Mock de API externa (SWAPI) |
| **H2** | Base de datos en memoria para pruebas |

## 📦 Despliegue en Heroku

```bash
# Crear app
heroku create nombre-app

# Configurar variables
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set DB_URL="jdbc:postgresql://..."
heroku config:set DB_USERNAME="usuario"
heroku config:set DB_PASSWORD="password"
heroku config:set JWT_SECRET="clave_secreta"

# Desplegar
git push heroku main

# Ver logs
heroku logs --tail -a nombre-app
```

## 🐳 Docker

### Construir imagen

```bash
docker build -t swapi-backend .
```

### Ejecutar contenedor

```bash
# Modo desarrollo (H2)
docker run -p 8080:8080 swapi-backend

# Modo produccion (PostgreSQL)
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://host:5432/db \
  -e DB_USERNAME=usuario \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=clave_secreta \
  swapi-backend
```


## 👨‍💻 Autor

<div align="center">

**Xavier Chavez**

[![Email](https://img.shields.io/badge/Email-xavierchavez916@gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:xavierchavez916@gmail.com)

</div>

