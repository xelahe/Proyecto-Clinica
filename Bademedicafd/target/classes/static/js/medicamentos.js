const form = document.querySelector("#medication-form");
const medicationId = document.querySelector("#medication-id");
const medicationList = document.querySelector("#medication-list");
const medicationCount = document.querySelector("#medication-count");
const feedback = document.querySelector("#feedback");
const formTitle = document.querySelector("#form-title");
const saveButton = document.querySelector("#save-button");
const cancelEditButton = document.querySelector("#cancel-edit");

const fields = ["nombre", "presentacion", "concentracion", "stock"];

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
    cell.textContent = value ?? "—";
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

function renderMedications(medications) {
    medicationList.replaceChildren();
    medicationCount.textContent = String(medications.length);

    if (medications.length === 0) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.className = "empty-state";
        cell.colSpan = 5;
        cell.textContent = "Aún no hay medicamentos registrados.";
        row.append(cell);
        medicationList.append(row);
        return;
    }

    for (const medication of medications) {
        const row = document.createElement("tr");
        addCell(row, medication.nombre);
        addCell(row, medication.presentacion);
        addCell(row, medication.concentracion);
        addCell(row, medication.stock);

        const actions = document.createElement("td");
        const actionGroup = document.createElement("div");
        actionGroup.className = "row-actions";
        addActionButton(actionGroup, "Editar", "button-secondary", () => editMedication(medication));
        addActionButton(actionGroup, "Eliminar", "button-delete", () => deleteMedication(medication));
        actions.append(actionGroup);
        row.append(actions);
        medicationList.append(row);
    }
}

async function loadMedications() {
    showFeedback("");
    try {
        const medications = await request("/api/medicamentos");
        renderMedications(medications);
    } catch (error) {
        showFeedback(error.message, true);
        medicationList.replaceChildren();
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.className = "empty-state";
        cell.colSpan = 5;
        cell.textContent = "No se pudo cargar la lista. Revisa que el servidor y MySQL estén disponibles.";
        row.append(cell);
        medicationList.append(row);
    }
}

function editMedication(medication) {
    medicationId.value = medication.id;
    for (const field of fields) {
        form.elements[field].value = medication[field] ?? "";
    }
    formTitle.textContent = "Editar medicamento";
    saveButton.textContent = "Guardar cambios";
    cancelEditButton.classList.remove("hidden");
    showFeedback(`Editando ${medication.nombre}.`);
    document.querySelector("#nombre").focus();
}

function resetForm() {
    form.reset();
    medicationId.value = "";
    formTitle.textContent = "Nuevo medicamento";
    saveButton.textContent = "Guardar medicamento";
    cancelEditButton.classList.add("hidden");
    showFeedback("");
}

async function saveMedication(event) {
    event.preventDefault();
    saveButton.disabled = true;
    showFeedback("");

    const medication = Object.fromEntries(fields.map(field => [
        field,
        ["concentracion", "stock"].includes(field)
            ? Number(form.elements[field].value || 0)
            : form.elements[field].value.trim()
    ]));
    const id = medicationId.value;
    const url = id ? `/api/medicamentos/${encodeURIComponent(id)}` : "/api/medicamentos";
    const method = id ? "PUT" : "POST";

    try {
        await request(url, {
            method,
            body: JSON.stringify(medication)
        });
        resetForm();
        await loadMedications();
        showFeedback(id ? "Medicamento actualizado correctamente." : "Medicamento creado correctamente.");
    } catch (error) {
        showFeedback(error.message, true);
    } finally {
        saveButton.disabled = false;
    }
}

async function deleteMedication(medication) {
    if (!window.confirm(`¿Eliminar el registro de ${medication.nombre}?`)) {
        return;
    }
    showFeedback("");
    try {
        await request(`/api/medicamentos/${encodeURIComponent(medication.id)}`, { method: "DELETE" });
        if (medicationId.value === String(medication.id)) {
            resetForm();
        }
        await loadMedications();
        showFeedback("Medicamento eliminado correctamente.");
    } catch (error) {
        showFeedback(error.message, true);
    }
}

form.addEventListener("submit", saveMedication);
cancelEditButton.addEventListener("click", resetForm);
document.querySelector("#refresh-button").addEventListener("click", loadMedications);
loadMedications();
