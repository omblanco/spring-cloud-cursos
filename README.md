# Spring Cloud Cursos
Spring Cloud Cursos

## Índice
- [Descripción](#descripción)
- [Componentes](#componentes)
- [Módulos](#módulos)
- [Uso](#uso)
	- [Requisitos](#requisitos)
	- [Generación de artefactos](#generación-de-artefactos)
	- [Lanzar la aplicación](#lanzar-la-aplicación)

## Descripción
Ejemplo de un ecosistema de microservicios desarrollado con la plataforma Spring Cloud, usando Eureka Server como servidor de registro y descubrimiento de microservicios y Spring Cloud Gateway como Api Gateway. Se trata la gestión de usuarios, cursos, exámenes y respuestas utilizando distintas bases de datos (MariaDB, PostgreSQL y MongoDB).

## Componentes
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- Spring Eureka Server
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Data Mongo DB](https://spring.io/projects/spring-data-mongodb#overview)


## Módulos

### commons-alumnos
Módulo con el modelos de alumnos.

### commons-examenes
Módulo con los modelos necesarios para gestionar exámenes.

### commons-microservicios
Módulo con clases genéricas de controladores y servicios para el desarrollo de microservicios.

### microservicios-cursos
Módulo que se encarga proporcionar los métodos rest para la gestión de los cursos. Utiliza MariaDB.

### microservicios-examenes
Módulo que gestiona los exámenes, también usa MariaDB.

### microservicios-respuestas
Gestiona las respuestas de los exámenes usando una bbdd no relacional MongoDB.

### microservicios-usuarios
Módulo responsable de la información de los usuarios, utiliza una base de datos relacional PostgreSQL.

### microservicios-eureka
Servidor de registro y descubrimiento de los microservicios. Se encarga de gestionar el balanceo si hay varias instancias de un microservicio.

### microservicios-gateway
Api Gateway que proporciona un punto de acceso común a todos los microservicios registrados en el Eureka Server.


## Uso

### Requisitos

- [Java JDK 11](https://www.oracle.com/es/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/)
- [MongoDB](https://docs.mongodb.com/manual/installation/)
- [MariaDB](https://mariadb.org/download/)
- [PostgreSQL](https://www.postgresql.org/)

### Generación de artefactos

#### Alternativa Maven

En el directorio raíz del proyecto lanzamos:

```
mvn clean package
```

Para omitir los tests:
```
mvn clean package -DskipTests=true
```

#### Alternativa sin Maven

En el directorio raíz del proyecto se hace uso del comando mvnw tanto para windows como para unix

```
mvnw clean package
```

### Lanzar la aplicación

El orden de despligue de los artefactos debe ser:
- microservicios-eureka
- microservicios-cursos
- microservicios-examenes
- microservicios-respuestas
- microservicios-usuarios
- microservicios-gateway


#### Alternativa Maven

Moverse al directorio:

```
nombre-modulo\target
```

Y ejecutamos el fichero jar según la aplicación:

```
java -jar nombre-jar-x.x.x.jar
```

#### Alternativa sin Maven

Lanzar las aplicaciones desde el directorio raíz:

```
mvnw -pl nombre-modulo -am spring-boot:run
```
