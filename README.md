# PokeApp

Aplicación full-stack desarrollada con **Spring Boot** en el backend y **Angular** en el frontend, que consulta datos en la API oficial de Pokémon.

---

## Tecnologías utilizadas

### Backend
- **Java 17+**
- **Spring Boot** (Spring Web, Spring Data JPA)
- **Maven**
- **Base de datos:** MySQL / H2

### Frontend
- **Angular**
- **TypeScript**
- **HTML5 / CSS3 / SCSS**

---

##  Estructura del repositorio

```text
Poke-App/
├── apppoke/          # Backend (Spring Boot API)
└── poketactics-ui/   # Frontend (Angular Application)

---

## Instalación y ejecución local

### Backend
cd apppoke/apppoke
./mvnw spring-boot:run

El servidor backend se ejecutará en http://localhost:8080.

### Frontend
cd poketactics-ui
npm install
ng serve

La aplicación cliente estará disponible en http://localhost:4200.

---