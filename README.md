# MuvU

Plataforma para que **estudiantes universitarios** encuentren y reserven habitaciones cerca de su universidad, y para que **arrendadores** publiquen y destaquen sus habitaciones.

Proyecto grupal del curso *Desarrollo Basado en Plataformas*.

## Stack

| | |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.1 (Web, Data JPA, Security, Validation, Mail, Thymeleaf) |
| Base de datos | PostgreSQL 16 (Docker) |
| Autenticación | JWT (JJWT 0.12) |
| Integraciones | Google Maps (geocodificación), Stripe (pagos), Gmail SMTP (correos) |

## Cómo ejecutarlo

1. **Base de datos**
   ```bash
   cd desarrollo
   docker compose up -d
   ```
   Levanta PostgreSQL en el puerto **5433** con la base `Muvu`.

2. **Variables de entorno**: copia `desarrollo/.env.example` a `desarrollo/.env` y completa:

   | Variable | Para qué |
   |---|---|
   | `JWT_SECRET` | Llave para firmar los tokens (mínimo 32 caracteres) |
   | `JWT_EXPIRATION` | Duración del token en ms (ej. `3600000` = 1 h) |
   | `GOOGLE_MAPS_API_KEY` | Geocodificar direcciones de habitaciones y universidades |
   | `STRIPE_SECRET_KEY` | Clave **de prueba** (`sk_test_...`) de Stripe |
   | `MAIL_USERNAME` / `MAIL_PASSWORD` | Cuenta de Gmail y su *contraseña de aplicación* |

3. **Ejecutar** desde IntelliJ (`DesarrolloApplication`) o con `./mvnw spring-boot:run`.
   La API queda en `http://localhost:8081`.

Al arrancar, `data.sql` carga un **administrador** y tres universidades (UTEC, PUCP, UNI).

| Usuario | Contraseña |
|---|---|
| `admin@muvu.com` | `admin123` |

## Roles

| Rol | Puede |
|---|---|
| `ESTUDIANTE` | Reservar habitaciones, cancelar sus reservas, calificar reservas confirmadas |
| `ARRENDADOR` | Publicar y editar sus habitaciones (una vez **verificado** por el admin), subir imágenes, confirmar reservas, pagar publicidad |
| `ADMIN` | Verificar arrendadores, crear y eliminar universidades, eliminar cuentas y calificaciones |

Cada usuario solo puede modificar **sus propios** datos; el resto recibe `403`.

## Endpoints principales

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/auth/register/estudiante`, `/auth/register/arrendador` | Público |
| POST | `/auth/login` | Público |
| GET | `/habitacion`, `/habitacion/{id}`, `/habitacion/cercanas?universidadId=&radioKm=` | Público |
| POST / PUT / DELETE | `/habitacion`, `/habitacion/{id}` | Arrendador dueño |
| POST / GET | `/habitacion/{id}/imagenes` | Arrendador dueño / público |
| POST | `/reservas` | Estudiante |
| GET | `/reservas` | Las reservas propias (el admin ve todas) |
| PATCH | `/reservas/{id}/confirmar` | Arrendador dueño |
| PATCH | `/reservas/{id}/cancelar` | Estudiante que reservó |
| POST | `/calificaciones` | Estudiante con reserva confirmada |
| GET | `/arrendador/{id}` | Autenticado |
| PUT | `/arrendador/{id}` | El propio arrendador |
| PATCH | `/arrendador/{id}/verificar` | Admin |
| POST | `/api/pagos-publicidad` | Arrendador dueño (30 = 30 días, 54 = 60 días) |
| GET / POST / DELETE | `/universidad` | Público / Admin / Admin |

Las rutas protegidas requieren el header `Authorization: Bearer <token>`.

## Reglas de negocio

- Una habitación no se puede reservar en fechas que se crucen con otra reserva **pendiente o confirmada**.
- Solo se califica una reserva **confirmada**, una sola vez.
- La publicidad pagada destaca la habitación; una tarea diaria la desactiva al vencer.

## Eventos asíncronos

Se procesan después del commit (`@TransactionalEventListener` + `@Async`), sin demorar la respuesta:

| Evento | Qué hace |
|---|---|
| `ActualizacionPromedioEvent` | Al crear o eliminar una calificación, recalcula `puntajePromedio` y `totalCalificaciones` del arrendador |
| `ActualizacionHabitacionesEvent` | Al crear o eliminar una habitación, recalcula `cantidadHabitaciones` del arrendador |
| `NotificacionCorreoEvent` | Envía un correo (plantilla `ThymeLeafMail.html`) |

Correos que se envían:

| Momento | Destinatario |
|---|---|
| Registro | El nuevo usuario (bienvenida) |
| Verificación del arrendador | Arrendador |
| Pago de publicidad aprobado | Arrendador |
| Reserva creada | Estudiante y arrendador |
| Reserva confirmada | Estudiante |
| Reserva cancelada | Arrendador |

## Pruebas

- **Unitarias** (Mockito, sin base de datos): `ReservaServiceTest`, `ArrendadorServiceTest`.
- **Postman**: importa `postman/MuvU.postman_collection.json` y ejecútala completa con el *Runner*; guarda sola los tokens, ids y fechas.
