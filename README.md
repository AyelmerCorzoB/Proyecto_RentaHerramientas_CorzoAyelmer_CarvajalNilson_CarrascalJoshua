# 🌟 **AlkileApp** 🌟

**AlkileApp** es una aplicación desarrollada en **Java** utilizando el framework **Spring Boot**. Su objetivo es proporcionar una solución robusta y escalable para gestionar el alquiler de herramientas entre proveedores y clientes de forma eficiente y segura.

---

## 🚀 **Características principales**

- 🔧 **Framework:** Spring Boot  
- 📦 **Gestión de dependencias:** Maven  
- 🔐 **Seguridad:** Spring Security con JWT y codificación de contraseñas `BCryptPasswordEncoder`  
- 👥 **Roles definidos:** `ADMIN`, `CUSTOMER`, `SUPPLIER`  
- ⚙️ **Inicialización automática:** Carga inicial de roles y usuarios con `CommandLineRunner`  
- 🌍 **CORS personalizado:** Acceso controlado desde `http://127.0.0.1:5500`  
- 🧩 **Arquitectura modular:** Separación clara entre capas `domain`, `application`, e `infrastructure`  
- 📑 **Documentación API:** Integración con Swagger UI  

---

## 📋 **Requisitos previos**

- **Java 17** o superior  
- **Maven 3.8** o superior  
- **Base de datos:** PostgreSQL (o compatible)  

---

## ⚙️ **Instalación**

1. **Clona este repositorio:**
   ```bash
   git clone https://github.com/AyelmerCorzoB/Proyecto_RentaHerramientas_CorzoAyelmer_CarvajalNilson_CarrascalJoshua.git
   cd Proyecto_RentaHerramientas_CorzoAyelmer_CarvajalNilson_CarrascalJoshua
   ```

2. **Configura la conexión a la base de datos en `src/main/resources/application.properties`:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:3306/alkileapp
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=create-drop
   ```

---

## ▶️ **Ejecución**
1. Abre el proyecto en IntelliJ IDEA o tu editor de preferencia.
2. Asegúrate de que el JDK esté configurado correctamente.
3. Busca la clase principal `AlkileAppApplication.java` y ejecútala.  
   O bien, puedes ejecutar el siguiente comando en la terminal:
```bash
mvn spring-boot:run
```

Una vez iniciada, estará disponible en:  
```
http://localhost:8080
```

## 🧪 ¿Cómo probar la aplicación?

1. **Regístrate** enviando una solicitud `POST` a `/api/auth/register` con tus datos de usuario, o **inicia sesión** en `/api/auth/login`.
2. O bien puedes entrar con los usuarios predefinidos que tenemos disponibles al ejecutar springboot.
    Ejemplo de solicitud ADMIN:
    ```json
    {
      "username": "admin",
      "password": "123456789"
    }
   ```
    Ejemplo de solicitud SUPPLIER:
    ```json
    {
      "username": "supplier",
      "password": "123456789"
    }
   ```
    Ejemplo de solicitud CUSTOMER:
    ```json
    {
      "username": "customer",
      "password": "123456789"
    }
   ```
3. **Recibe un token JWT** en la respuesta al iniciar sesión o registrarte.  
   Ejemplo de respuesta:
   ```json
   {
     "token": "TOKEN_DE_ACCESO",
     "role": "CUSTOMER"
   }
   ```
4. **Incluye el token** en el encabezado `Authorization` de tus solicitudes a endpoints protegidos:
   ```
   Authorization: Bearer TU_TOKEN_JWT
   ```
5. **Accede y prueba los endpoints** de la API utilizando herramientas como **Postman** o **Swagger UI**.
6. **Realiza operaciones CRUD** sobre herramientas, reservas, pagos, reportes de daño, etc.
7. **Ejemplo de respuesta de una categoría petición GET:**
   ```json
   {
     "id": 1,
     "name": "Herramientas Manuales",
     "description": "Herramientas que se operan manualmente sin necesidad de energia electrica",
     "tools": [
       {
         "id": 1,
         "name": "Martillo de carpintero",
         "description": "Martillo profesional con mango de fibra de vidrio",
         "dailyCost": 15.5,
         "stock": 3,
         "categoryId": 1,
         "supplier": {
           "id": 3,
           "taxId": "1234567890123",
           "company": "Herramientas Pro S.A.",
           "rating": 4.5,
           "userId": 3
         }
       }
     ]
   }
   ```

---

## 🔐 **Seguridad y autenticación**

- **JWT** para autenticación sin estado (stateless).  
- **Rutas públicas:** `/api/auth/**`.  
- **Rutas protegidas:** Requieren token JWT y están bajo `/api/alkile/**`.  
- **Codificación de contraseñas:** `BCryptPasswordEncoder`.  
- **Usuarios y roles iniciales:** Se cargan automáticamente al arrancar la app si no existen.  

---

## 📑 **Documentación con Swagger**

La API REST cuenta con documentación interactiva gracias a **Swagger UI**.  
Accede desde:  
```
http://localhost:8080/swagger-ui/index.html
```

> **Nota:** Swagger permite probar todos los endpoints disponibles, ver sus esquemas de entrada/salida y gestionar autenticación con JWT.

---

## 📂 **Estructura del proyecto**

```Estructura de paquetes 
alkile_app
├── application
│   ├── config              # Configuración de seguridad y datos iniciales
│   ├── security            # Filtro JWT y punto de entrada
│   └── services            # Interfaces y lógica de negocio
├── domain
│   ├── dto                 # Objetos de transferencia de datos (ToolDto, SupplierDto, etc.)
│   ├── entities            # Entidades JPA como User, Role, Tool, Reservation, DamageReport, etc.
│   └── exceptions          # Manejador global de excepciones con @RestControllerAdvice
├── infrastructure
│   ├── controllers         # Controladores REST (ToolController, DamageReportController, PaymentController, etc.)
│   └── repository          # Repositorios JPA
└── AlkileAppApplication.java
```

---

## 🔐 **Autenticación con JWT**

La aplicación utiliza **JSON Web Tokens (JWT)** para manejar la autenticación de forma segura y sin estado (stateless). A continuación, se describe cómo funciona el proceso:


### **Proceso de Autenticación**

1. **Inicio de Sesión**  
   - El usuario envía sus credenciales (email y contraseña) al endpoint:  
     ```http
     POST /api/auth/login
     ```
   - Si las credenciales son válidas, el servidor genera un token JWT y lo devuelve en la respuesta.

2. **Uso del Token**  
   - El cliente debe incluir el token en el encabezado `Authorization` de cada solicitud protegida:  
     ```
     Authorization: Bearer <tu_token_jwt>
     ```

3. **Validación del Token**  
   - El servidor valida el token en cada solicitud utilizando el filtro `JwtAuthenticationFilter`.  
   - Si el token es válido, se autentica al usuario y se le permite acceder al recurso solicitado.

### **Componentes Clave**

- **SecurityConfig:**  
  Configura la seguridad de la aplicación, incluyendo la protección de rutas y la configuración de CORS.  
  - **HttpSecurity:** Define las reglas de seguridad para las rutas, permitiendo el acceso a las rutas públicas y protegiendo las privadas.  
  - **PasswordEncoder:** Utiliza `BCryptPasswordEncoder` para codificar las contraseñas de los usuarios.

- **JwtAuthenticationFilter:**  
  Este filtro intercepta las solicitudes HTTP y valida el token JWT. Si el token es válido, autentica al usuario en el contexto de seguridad de Spring.  

- **JwtAuthEntryPoint:**  
  Maneja los errores de autenticación devolviendo un código `401 Unauthorized` cuando el token es inválido o no está presente.  

- **JwtService:**  
  Este servicio se encarga de generar, firmar y validar los tokens JWT. También extrae información como el nombre de usuario y la fecha de expiración del token.

### **Configuración del Token**

En el archivo `application.properties`, puedes configurar el secreto y la expiración del token:
```properties
jwt.secret=tu_secreto_base64
jwt.expiration=3600000 # 1 hora en milisegundos
```

### **Endpoints Públicos y Protegidos**

- **Endpoints Públicos:**  
  No requieren autenticación. Ejemplo:  

  ### **Autenticación**
- **POST** `/api/auth/login` → Autenticación de usuario  
- **POST** `/api/auth/register` → Registro de nuevos usuarios  

  ### **Documentación**
- **GET** `/swagger-ui/index.html` → Documentación de la API (Swagger UI)



- 📮**Endpoints Protegidos:**  
  Requieren un token JWT válido. Ejemplo:  

  ### **Herramientas**
- **GET** `/api/alkile/tools` → Obtener lista de herramientas (requiere token)  
- **POST** `/api/alkile/tools` → Registrar herramienta
- **PUT** `/api/alkile/tools/{id}` → Actualizar herramienta
- **DELETE** `/api/alkile/tools/{id}` → Eliminar herramienta
- **GET** `/api/alkile/tools/{id}` → Obtener herramienta por ID  

### **Reportes de daño**
- **GET** `/api/alkile/damage-reports` → Ver todos los reportes  
- **POST** `/api/alkile/damage-reports` → Crear reporte de daño  
- **PUT** `/api/alkile/damage-reports/{id}` → Actualizar reporte  
- **DELETE** `/api/alkile/damage-reports/{id}` → Eliminar reporte  
- **GET** `/api/alkile/damage-reports/{id}` → Obtener reporte por ID


### **Pagos y Facturas**
- **GET** `/api/alkile/payments` → Ver pagos  
- **POST** `/api/alkile/payments` → Crear pago (genera factura automáticamente)  
- **PUT** `/api/alkile/payments/{id}` → Actualizar pago  
- **DELETE** `/api/alkile/payments/{id}` → Eliminar pago  
- **GET** `/api/alkile/payments/{id}` → Obtener pago por ID

### **Reservas**
- **GET** `/api/alkile/reservations` → Obtener todas las reservas  
- **POST** `/api/alkile/reservations` → Crear una nueva reserva  
- **PUT** `/api/alkile/reservations/{id}` → Actualizar reserva
- **DELETE** `/api/alkile/reservations/{id}` → Eliminar reserva
- **GET** `/api/alkile/reservations/{id}` → Obtener reserva por ID


### **Categorías**
- **GET** `/api/alkile/categories` → Lista de categorías disponibles  
- **POST** `/api/alkile/categories` → Crear nueva categoría  
- **PUT** `/api/alkile/categories/{id}` → Actualizar categoría
- **DELETE** `/api/alkile/categories/{id}` → Eliminar categoría
- **GET** `/api/alkile/categories/{id}` → Obtener categoría por ID


> **Tip:** Puedes probar los endpoints protegidos utilizando herramientas como **Postman** o **Swagger UI**.

---

## 🏗️ Entidades y relaciones

Las entidades están construidas con JPA (Hibernate), usando anotaciones como `@Entity`, `@OneToMany`, `@ManyToOne`, `@JoinColumn`.

### 🧱 Entidades

#### User

- **Campos:**
    - `id`: Identificador único del usuario
    - `username`: Nombre de usuario único
    - `email`: Correo electrónico único
    - `password`: Contraseña cifrada
    - `name`: Nombre completo
    - `phone`: Teléfono
    - `address`: Dirección
    - `registrationDate`: Fecha de registro
    - `active`: Estado de la cuenta (activo/inactivo)
    - `roles`: Roles asignados al usuario (`ADMIN`, `CUSTOMER`, `SUPPLIER`)

- **Relaciones:**
    - `@ManyToMany` con `Role`
    - `@OneToOne` con `Supplier`
    - `@OneToOne` con `Customer`

#### Role

- **Campos:**
    - `id`: Identificador único del rol
    - `name`: Nombre del rol (`ADMIN`, `CUSTOMER`, `SUPPLIER`)
    - `description`: Descripción del rol

- **Relaciones:**
    - `@ManyToMany` bidireccional con `User`

#### Tool

- **Campos:**
    - `id`: Identificador único de la herramienta
    - `supplier`: Proveedor dueño de la herramienta
    - `category`: Categoría a la que pertenece
    - `name`: Nombre de la herramienta
    - `description`: Descripción detallada
    - `dailyCost`: Costo diario de alquiler
    - `stock`: Cantidad disponible
    - `imageUrl`: URL de la imagen
    - `creationDate`: Fecha de creación del registro
    - `available`: Disponibilidad (true/false)
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@ManyToOne` con `Supplier`
    - `@ManyToOne` con `Category`
    - `@OneToMany` con `Reservation`

#### Reservation

- **Campos:**
    - `id`: Identificador único de la reserva
    - `customer`: Cliente que realiza la reserva
    - `tool`: Herramienta reservada
    - `startDate`: Fecha de inicio de la reserva
    - `endDate`: Fecha de fin de la reserva
    - `status`: Estado de la reserva (`PENDING`, `APPROVED`, `REJECTED`, `COMPLETED`, `CANCELED`)
    - `creationDate`: Fecha de creación del registro
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@ManyToOne` con `Customer`
    - `@ManyToOne` con `Tool`
    - `@OneToOne` con `Payment`
    - `@OneToOne` con `DamageReport`

#### Payment

- **Campos:**
    - `id`: Identificador único del pago
    - `reservation`: Reserva asociada al pago
    - `amount`: Monto pagado
    - `paymentMethod`: Método de pago utilizado
    - `transactionId`: ID de la transacción (opcional)
    - `paymentDate`: Fecha del pago
    - `status`: Estado del pago (`COMPLETED`, `PENDING`, `FAILED`)
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@OneToOne` con `Reservation`

#### DamageReport

- **Campos:**
    - `id`: Identificador único del reporte de daño
    - `reservation`: Reserva asociada al reporte
    - `description`: Descripción del daño
    - `repairCost`: Costo estimado de reparación
    - `reportDate`: Fecha del reporte
    - `resolved`: Estado del reporte (resuelto/no resuelto)
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@OneToOne` con `Reservation`

#### Return

- **Campos:**
    - `id`: Identificador único de la devolución
    - `reservation`: Reserva asociada a la devolución
    - `returnDate`: Fecha de la devolución
    - `comments`: Comentarios adicionales sobre la devolución

- **Relaciones:**
    - `@OneToOne` con `Reservation`

#### Category

- **Campos:**
    - `id`: Identificador único de la categoría
    - `name`: Nombre de la categoría (único)
    - `description`: Descripción de la categoría
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@OneToMany` con `Tool`

#### Audit

- **Campos:**
    - `createdAt`: Fecha y hora de creación del registro
    - `updatedAt`: Fecha y hora de la última actualización

- **Uso:**
    - Se utiliza como campo embebido (`@Embedded`) en otras entidades para registrar información de auditoría (creación/modificación)

#### Customer

- **Campos:**
    - `id`: Identificador único del cliente
    - `taxId`: Identificación tributaria (máx. 13 caracteres)
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@OneToOne` con `User`

#### CustomerPreference

- **Campos:**
    - `id`: Identificador único de la preferencia
    - `preference`: Preferencia del cliente (máx. 100 caracteres)

- **Relaciones:**
    - `@ManyToOne` con `Customer`
    - Restricción única sobre la combinación de `customer` y `preference`

#### Invoice

- **Campos:**
    - `id`: Identificador único de la factura
    - `issueDate`: Fecha de emisión de la factura
    - `details`: Detalles adicionales de la factura

- **Relaciones:**
    - `@OneToOne` con `Payment`

#### Notification

- **Campos:**
    - `id`: Identificador único de la notificación
    - `message`: Mensaje de la notificación
    - `read`: Estado de lectura (true/false)
    - `creationDate`: Fecha de creación de la notificación
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@ManyToOne` con `User`

#### Supplier

- **Campos:**
    - `id`: Identificador único del proveedor
    - `taxId`: Identificación tributaria (máx. 13 caracteres)
    - `company`: Nombre de la empresa (máx. 100 caracteres)
    - `rating`: Calificación del proveedor (valor numérico, por defecto 0.0)
    - `audit`: Información de auditoría (creación/modificación)

- **Relaciones:**
    - `@OneToOne` con `User`

---


## 🧰 **DTO y Excepciones**

- **DTOs:** Como `ToolDto`, `DamageReportDto`, `PaymentDto` encapsulan la información relevante entre cliente y servidor.  
- **Conversión:** Cada controller realiza la conversión entre entidades JPA y DTOs mediante métodos `toDto` y `toEntity`.  
- **Relaciones:** Se manejan relaciones entre entidades, como `Reservation` asociada a `Payment` y `DamageReport`.  

⚠️ **Manejador global de errores:**  
Se encuentra en `GlobalExceptionHandler`.  
Devuelve objetos `ApiError` con información clara y útil de los errores, incluidos errores de JWT y validaciones.

---

## 👥 **Autores**

- Ayelmer Corzo  
- Nilson Carvajal  
- Joshua Carrascal  
