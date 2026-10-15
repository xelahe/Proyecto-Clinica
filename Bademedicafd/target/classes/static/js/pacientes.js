// Referencias al formulario, la tabla y los controles que se actualizan durante el CRUD.
const form = document.querySelector("#patient-form");
const patientId = document.querySelector("#patient-id");
const patientList = document.querySelector("#patient-list");
const patientCount = document.querySelector("#patient-count");
const feedback = document.querySelector("#feedback");
const formTitle = document.querySelector("#form-title");
const saveButton = document.querySelector("#save-button");
const cancelEditButton = document.querySelector("#cancel-edit");

const fields = [
    "nombre",
    "documento",
    "contacto",
    "direccion",
    "condicionSalud",
    "necesidadesCuidado"
];

/** Muestra un mensaje de estado y aplica el estilo de error cuando corresponde. */
function showFeedback(message, isError = false) {
    feedback.textContent = message;
    feedback.classList.toggle("error", isError);
}

/** Ejecuta una solicitud JSON al API y convierte las respuestas no exitosas en errores visibles. */
async function request(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        }
    });

    if (!response.ok) {
        let message = `La solicitud falló (${response.status}).`;
        try {
            const body = await response.json();
            message = body.error || body.detail || message;
        } catch {
            // La respuesta puede no incluir contenido JSON.
        }
        throw new Error(message);
    }

    return response.status === 204 ? null : response.json();
}

/** Crea una celda de tabla usando texto para mantener los datos como contenido plano. */
function addCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = value || "—";
    row.append(cell);
}

/** Crea un botón de acción y conecta el manejador correspondiente. */
function addActionButton(container, label, className, onClick) {
    const button = document.createElement("button");
    button.type = "button";
    button.className = `button ${className}`;
    button.textContent = label;
    button.addEventListener("click", onClick);
    container.append(button);
}

/** Actualiza el contador y dibuja las filas del directorio, incluida la vista vacía. */
function renderPatients(patients) {
    patientList.replaceChildren();
    patientCount.textContent = String(patients.length);

    if (patients.length === 0) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.className = "empty-state";
        cell.colSpan = 6;
        cell.textContent = "Aún no hay pacientes registrados.";
        row.append(cell);
        patientList.append(row);
        return;
    }

    for (const patient of patients) {
        const row = document.createElement("tr");
        addCell(row, patient.nombre);
        addCell(row, patient.documento);
        addCell(row, patient.contacto);
        addCell(row, patient.direccion);
        addCell(row, patient.condicionSalud);

        const actions = document.createElement("td");
        const actionGroup = document.createElement("div");
        actionGroup.className = "row-actions";
        addActionButton(actionGroup, "Editar", "button-secondary", () => editPatient(patient));
        addActionButton(actionGroup, "Eliminar", "button-delete", () => deletePatient(patient));
        actions.append(actionGroup);
        row.append(actions);
        patientList.append(row);
    }
}

/** Recupera el directorio desde el API y muestra una guía si la consulta falla. */
async function loadPatients() {
    showFeedback("");
    try {
        const patients = await request("/api/pacientes");
        renderPatients(patients);
    } catch (error) {
        showFeedback(error.message, true);
        patientList.replaceChildren();
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.className = "empty-state";
        cell.colSpan = 6;
        cell.textContent = "No se pudo cargar la lista. Revisa que el servidor y MySQL estén disponibles.";
        row.append(cell);
        patientList.append(row);
    }
}

/** Copia el paciente seleccionado al formulario y activa el modo de edición. */
function editPatient(patient) {
    patientId.value = patient.id;
    for (const field of fields) {
        form.elements[field].value = patient[field] || "";
    }
    formTitle.textContent = "Editar paciente";
    saveButton.textContent = "Guardar cambios";
    cancelEditButton.classList.remove("hidden");
    showFeedback(`Editando a ${patient.nombre}.`);
    document.querySelector("#nombre").focus();
}

/** Limpia el formulario y restaura los textos y controles del modo de alta. */
function resetForm() {
    form.reset();
    patientId.value = "";
    formTitle.textContent = "Nuevo paciente";
    saveButton.textContent = "Guardar paciente";
    cancelEditButton.classList.add("hidden");
    showFeedback("");
}

/** Envía los campos como alta o actualización según exista un identificador seleccionado. */
async function savePatient(event) {
    event.preventDefault();
    saveButton.disabled = true;
    showFeedback("");

    const patient = Object.fromEntries(fields.map(field => [
        field,
        form.elements[field].value.trim()
    ]));
    const id = patientId.value;
    const url = id ? `/api/pacientes/${encodeURIComponent(id)}` : "/api/pacientes";
    const method = id ? "PUT" : "POST";

    try {
        await request(url, {
            method,
            body: JSON.stringify(patient)
        });
        resetForm();
        await loadPatients();
        showFeedback(id ? "Paciente actualizado correctamente." : "Paciente creado correctamente.");
    } catch (error) {
        showFeedback(error.message, true);
    } finally {
        saveButton.disabled = false;
    }
}

/** Solicita confirmación antes de borrar y actualiza la tabla tras completar la operación. */
async function deletePatient(patient) {
    if (!window.confirm(`¿Eliminar el registro de ${patient.nombre}?`)) {
        return;
    }
    showFeedback("");
    try {
        await request(`/api/pacientes/${encodeURIComponent(patient.id)}`, { method: "DELETE" });
        if (patientId.value === String(patient.id)) {
            resetForm();
        }
        await loadPatients();
        showFeedback("Paciente eliminado correctamente.");
    } catch (error) {
        showFeedback(error.message, true);
    }
}

form.addEventListener("submit", savePatient);
cancelEditButton.addEventListener("click", resetForm);
document.querySelector("#refresh-button").addEventListener("click", loadPatients);
loadPatients();
