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

function showFeedback(message, isError = false) {
    feedback.textContent = message;
    feedback.classList.toggle("error", isError);
}

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

function addCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = value || "—";
    row.append(cell);
}

function addActionButton(container, label, className, onClick) {
    const button = document.createElement("button");
    button.type = "button";
    button.className = `button ${className}`;
    button.textContent = label;
    button.addEventListener("click", onClick);
    container.append(button);
}

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

function resetForm() {
    form.reset();
    patientId.value = "";
    formTitle.textContent = "Nuevo paciente";
    saveButton.textContent = "Guardar paciente";
    cancelEditButton.classList.add("hidden");
    showFeedback("");
}

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
