# 🚀 API REST con Spring Boot y PostgreSQL 17

Este proyecto es una **API REST** desarrollada con **Spring Boot** y **PostgreSQL 17**. A continuación, se explican los pasos para **configurar, instalar y ejecutar** correctamente el microservicio.

---

## 📌 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:

- [Java 17](https://adoptium.net/)
- [Gradle](https://gradle.org/install/)
- [PostgreSQL 17](https://www.postgresql.org/download/)
- [pgAdmin](https://www.pgadmin.org/) (Opcional, para administrar la base de datos visualmente)

---

## 🛠 Instalación y Configuración de PostgreSQL 17

1. **Descargar PostgreSQL** desde su sitio oficial:  
   🔗 [Descargar PostgreSQL](https://www.postgresql.org/download/)

2. **Durante la instalación, se te pedirá:**
   - **Usuario administrador (superusuario):** `postgres`
   - **Contraseña:** *(elige una y recuérdala, ya que será necesaria para acceder a la base de datos).*
   - **Puerto:** `5432` *(predeterminado, no cambiar a menos que sea necesario).*

📌 **Nota:** Una vez instalado, **PostgreSQL estará accesible en `localhost:5432`**, con el usuario `postgres` y la contraseña definida durante la instalación.  

---

## 🛠 Creación de la Base de Datos en pgAdmin

Si deseas administrar PostgreSQL visualmente con **pgAdmin**, sigue estos pasos:

### 🔹 **1. Abrir pgAdmin y Conectarse al Servidor**
1. **Abre pgAdmin** e inicia sesión.
2. **PostgreSQL ya está configurado en `localhost:5432`**, solo necesitas iniciar sesión con:
   - **Usuario:** `postgres`
   - **Contraseña:** *(la definida durante la instalación de PostgreSQL)*

---

### 🔹 **2. Crear la Base de Datos**
1. En el panel izquierdo, haz clic derecho sobre **Databases** y selecciona **Create → Database**.
2. **En la pestaña "General"**, coloca los siguientes datos:
   - **Database**: `prueba`
   - **Owner**: `postgres` (usuario predeterminado)
3. **Haz clic en "Save"** para crear la base de datos.

📌 **Con esto, ya tienes PostgreSQL listo para conectar tu aplicación.**

---

## 🛠 Configurar el Acceso a la Base de Datos en Spring Boot

Después de crear la base de datos, es necesario configurar la conexión en el archivo `application.properties` para que la aplicación pueda acceder a PostgreSQL.

### 🔹 **Editar `application.properties`**

Abre el archivo **`src/main/resources/application.properties`** y coloca los siguientes valores:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/prueba
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 🔹 **3. Ejecutar el Proyecto e Ingresar las Credenciales**

Una vez configurado el acceso a la base de datos, ejecuta el proyecto, luego usa las siguientes credenciales para acceder:

**Usuario:** `admin`  
**Contraseña:** `admin123`

### 🔹 **4. Visualizar la Documentación en Swagger**

Para acceder a la documentación interactiva de la API, abre el siguiente enlace en tu navegador:

📌 **URL de Swagger:**  
[`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

Desde aquí, podrás explorar y probar los endpoints de la API de manera sencilla.




