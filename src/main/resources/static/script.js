const taskForm = document.getElementById("taskForm");

// ✅ EDIT MODE VARIABLES
let editMode = false;
let editTaskId = null;


// ✅ CLEAR ERRORS
function clearErrors() {
    document.querySelectorAll(".error").forEach(e => e.innerText = "");
    document.querySelectorAll("input, select").forEach(el => {
        el.classList.remove("error-border");
    });
}


// ✅ SUBMIT FORM (CREATE + UPDATE)
taskForm.addEventListener("submit", async function(event) {

    event.preventDefault();
    clearErrors();

    const task = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        priority: document.getElementById("priority").value,
        status: document.getElementById("status").value,
        dueDate: document.getElementById("dueDate").value
    };

    // ✅ VALIDATION
    let isValid = true;

    const title = document.getElementById("title");
    const priority = document.getElementById("priority");
    const status = document.getElementById("status");
    const dueDate = document.getElementById("dueDate");

    if (!title.value) {
        document.getElementById("titleError").innerText = "Title is required";
        title.classList.add("error-border");
        isValid = false;
    }

    if (!priority.value) {
        document.getElementById("priorityError").innerText = "Select a priority";
        priority.classList.add("error-border");
        isValid = false;
    }

    if (!status.value) {
        document.getElementById("statusError").innerText = "Select a status";
        status.classList.add("error-border");
        isValid = false;
    }

    if (!dueDate.value) {
        document.getElementById("dueDateError").innerText = "Select a date";
        dueDate.classList.add("error-border");
        isValid = false;
    }

    if (!isValid) return;

    try {

        // ✅ IMPORTANT: Decide POST or PUT
        let url = "http://localhost:8080/tasks";
        let method = "POST";

        if (editMode) {
            url = `http://localhost:8080/tasks/${editTaskId}`;
            method = "PUT";
        }

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(task)
        });

        if (response.ok) {

            // ✅ SUCCESS MESSAGE
            showToast(editMode ? "Task Updated ✅" : "Task Added ✅");

            // ✅ RESET EDIT MODE
            editMode = false;
            editTaskId = null;

            document.querySelector("button[type='submit']").innerText = "Add Task";

            taskForm.reset();
        } else {
            
if (!response.ok) {
    let msg = "Something went wrong";

    try {
        const error = await response.json();
        msg = error.message || JSON.stringify(error);
    } catch (e) {
        const text = await response.text();
        msg = text;
    }

    console.error(msg);
    showToast(msg);
    return;
}

        }

    } catch (error) {
        console.error(error);
        showToast("Something went wrong");
    }
});


// ✅ LOAD TASKS
async function loadTasks(filter = "ALL") {

    try {
        const response = await fetch("http://localhost:8080/tasks");
        const tasks = await response.json();

        let filteredTasks = tasks;

        if (filter === "PENDING") {
            filteredTasks = tasks.filter(t => t.status === "PENDING");
        } else if (filter === "COMPLETED") {
            filteredTasks = tasks.filter(t => t.status === "COMPLETED");
        } else if (filter === "HIGH") {
            filteredTasks = tasks.filter(t => t.priority === "HIGH");
        }

        displayTasks(filteredTasks);

    } catch (error) {
        console.error(error);
    }
}


// ✅ DISPLAY TASKS
function displayTasks(tasks) {

    const taskList = document.getElementById("taskList");
    taskList.innerHTML = "";

    if (tasks.length === 0) {
        taskList.innerHTML = "<p>No tasks found 😔</p>";
        return;
    }

    tasks.forEach(task => {

        const div = document.createElement("div");
        div.className = "task " +
        task.priority.toLowerCase();

        div.innerHTML = `
            <h3>${task.title}</h3>
            <p>${task.description}</p>
            <p><b>Priority:</b> ${task.priority}</p>
            <p><b>Status:</b> ${task.status}</p>
            <p><b>Due:</b> ${new Date(task.dueDate).toLocaleDateString()}</p>

            <button onclick="editTask(${task.id})">✏️ Edit</button>
            <button onclick="deleteTask(${task.id})">❌ Delete</button>
        `;

        taskList.appendChild(div);
    });
}


// ✅ DELETE TASK
async function deleteTask(id) {

    if (!confirm("Are you sure you want to delete this task?")) return;

    try {
        const response = await fetch(`http://localhost:8080/tasks/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            showToast("Task deleted ✅");
            loadTasks();
        } else {
            showToast("Delete failed");
        }

    } catch (error) {
        console.error(error);
    }
}


// ✅ EDIT TASK
function editTask(id) {

    fetch(`http://localhost:8080/tasks`)
        .then(res => res.json())
        .then(tasks => {

            const task = tasks.find(t => t.id === id);

            // ✅ fill form
            document.getElementById("title").value = task.title;
            document.getElementById("description").value = task.description;
            document.getElementById("priority").value = task.priority;
            document.getElementById("status").value = task.status;
            document.getElementById("dueDate").value = task.dueDate;

            // ✅ switch view
            showForm();

            // ✅ ENABLE EDIT MODE
            editMode = true;
            editTaskId = id;

            document.querySelector("button[type='submit']").innerText = "Update Task";
        });
}


// ✅ PAGE LOAD
window.onload = function () {
    document.getElementById("taskList").style.display = "none";
    checkReminders();
};


// ✅ SHOW TASKS PAGE
function showTasks(filter) {

    document.getElementById("formSection").style.display = "none";
    document.getElementById("taskList").style.display = "block";
    document.getElementById("addTaskBtn").style.display = "block";

    loadTasks(filter);
}


// ✅ SHOW FORM PAGE
function showForm() {

    document.getElementById("formSection").style.display = "block";
    document.getElementById("taskList").style.display = "none";
    document.getElementById("addTaskBtn").style.display = "none";
}
function showToast(message) {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.className = "toast show";

    setTimeout(() => {
        toast.className = "toast";
    }, 3000);
}
// ✅ CHECK REMINDERS
async function checkReminders() {

    try {
        const response = await fetch("http://localhost:8080/tasks/reminders");
        const tasks = await response.json();

        if (tasks.length > 0) {
            showToast(`🔔 You have ${tasks.length} high priority task(s) today`);
        }

    } catch (error) {
        console.error("Reminder error:", error);
    }
}
// ✅ RUN EVERY 30 SECONDS
setInterval(checkReminders, 30000);
