# Plataforma de Atención Médica en Casa — Capa de datos (CRUD)

Proyecto Java 17 + Maven + JDBC sobre MySQL (`BaseDatoMedicaFD`).

## Cómo ejecutarlo
1. Cargar la base de datos: `mysql -u root -p < sql/Basemedicafd2.sql`
2. (Opcional) configurar la conexión con variables de entorno: `DB_URL`, `DB_USER`, `DB_PASSWORD`
   (por defecto: `localhost:3306`, usuario `root`, sin contraseña).
3. Ejecutar el demo: `mvn compile exec:java`
4. Ejecutar pruebas: `mvn test`

## Estructura
- `modelo/`     Clases del diagrama UML (con getters/setters y claves foráneas).
- `servicios/`  Interfaz `CRUD`, un DAO por entidad, `Validador` (reglas de negocio) y `Conexion`.
- `DemoCrud`    Recorre create/read/update/delete de cada entidad.

## Entidades con CRUD completo
| DAO | Tablas | Historia |
|---|---|---|
| `PacienteDAO` | Usuario + Paciente + HistorialClinico | HU01 |
| `PersonalDAO` | Usuario + Personal (+ Medico) | HU02 |
| `DisponibilidadDAO` | Disponibilidad | HU02 |
| `EspecialidadDAO` | Especialidad | HU02 |
| `CitaMedicaDAO` | CitaMedica | HU03 / HU04 |
| `MedicamentoDAO` | Medicamento | HU14 |
