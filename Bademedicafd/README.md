# Plataforma de Atención Médica en Casa

Proyecto Java 17 + Maven + Spring Boot, JDBC y MySQL (`BaseDatoMedicaFD`). Incluye una interfaz web para gestionar pacientes.

## Ejecutar la aplicación web
1. Iniciar MySQL y crear la base de datos `BaseDatoMedicaFD` con las tablas del proyecto.
   El script `sql/Basemedicafd2.sql` crea el esquema completo, pero **elimina primero
   cualquier base existente con ese nombre**. Ejecútalo solo si quieres reiniciar la
   base o si todavía no contiene datos que debas conservar.
2. La conexión JDBC usa por defecto `localhost:3306/BaseDatoMedicaFD`. Si tus datos de
   acceso son distintos, define las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` en
   la misma terminal desde la que iniciarás Maven.

   En PowerShell, por ejemplo:

   ```powershell
   $env:DB_URL = "jdbc:mysql://localhost:3306/BaseDatoMedicaFD?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota"
   $env:DB_USER = "tu_usuario"
   $env:DB_PASSWORD = "tu_contraseña"
   ```

   No guardes contraseñas reales en el código ni en archivos versionados.
3. Desde esta carpeta, ejecutar `mvn spring-boot:run`.
4. Abrir `http://localhost:8080` en el navegador. Las operaciones de pacientes y
   medicamentos accederán a MySQL por medio de sus DAO JDBC.

La aplicación sirve la interfaz desde `src/main/resources/static/` y expone el CRUD de pacientes en `/api/pacientes`. La lista y las operaciones de guardar, editar y eliminar requieren que MySQL esté activo y configurado.

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
