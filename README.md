# Plataforma de Atención Médica en Casa

Proyecto Java 17 + Maven + Spring Boot y JDBC. Incluye una interfaz web para gestionar pacientes y medicamentos.

## Ejecutar la aplicación web
1. No necesitas instalar una base de datos para probar la aplicación: por defecto usa
   H2 en memoria y crea las tablas que requieren los CRUD web de pacientes y medicamentos.
   Los datos se borran al cerrar la aplicación.
2. Desde la carpeta `Bademedicafd`, ejecuta `mvn spring-boot:run`.
3. Abre `http://localhost:8080` en el navegador.

Para usar MySQL en lugar de H2, crea la base `BaseDatoMedicaFD` con el esquema del
script `sql/Basemedicafd2.sql` (ten en cuenta que el script elimina la base existente).
Configura estas variables en la terminal antes de iniciar Maven:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/BaseDatoMedicaFD?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota"
$env:DB_USER = "tu_usuario"
$env:DB_PASSWORD = "tu_contraseña"
```

No guardes contraseñas reales en el código ni en archivos versionados. Al usar H2,
no definas `DB_URL` para MySQL.

La aplicación sirve la interfaz desde `src/main/resources/static/` y expone el CRUD de
pacientes en `/api/pacientes`. También permite gestionar medicamentos desde su interfaz.

> **Importante:** este prototipo no incluye autenticación ni control de acceso. Úsalo solo en desarrollo local y no lo publiques ni lo uses con datos clínicos reales hasta incorporar seguridad.

Para ejecutar las pruebas: `mvn test`.

## Estructura
- `src/main/java/co/edu/poli/atencionmedica/modelo/`     Modelos del sistema.
- `src/main/java/co/edu/poli/atencionmedica/servicios/`  DAO, validaciones y conexión JDBC.
- `src/main/java/co/edu/poli/atencionmedica/controladores/` API HTTP para el front.
- `src/main/resources/static/` Interfaz HTML, CSS y JavaScript.
- `DemoCrud`    Programa de demostración del CRUD por consola.

## API de pacientes
| Método | Ruta | Operación |
|---|---|---|
| `GET` | `/api/pacientes` | Listar pacientes |
| `GET` | `/api/pacientes/{id}` | Consultar paciente |
| `POST` | `/api/pacientes` | Crear paciente |
| `PUT` | `/api/pacientes/{id}` | Actualizar paciente |
| `DELETE` | `/api/pacientes/{id}` | Eliminar paciente |

Para crear o actualizar, enviar JSON con `nombre`, `documento`, `contacto`, `direccion`, `condicionSalud` y `necesidadesCuidado`. Los campos de salud se pueden dejar vacíos.

## Entidades con CRUD completo
| DAO | Tablas | Historia |
|---|---|---|
| `PacienteDAO` | Usuario + Paciente + HistorialClinico | HU01 |
| `PersonalDAO` | Usuario + Personal (+ Medico) | HU02 |
| `DisponibilidadDAO` | Disponibilidad | HU02 |
| `EspecialidadDAO` | Especialidad | HU02 |
| `CitaMedicaDAO` | CitaMedica | HU03 / HU04 |
| `MedicamentoDAO` | Medicamento | HU14 |
