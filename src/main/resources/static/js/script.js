function updateStatus(idTodo, button) {
    console.info("Function update terpanggil");
    const request = new Request(`/api/todos/${idTodo}`, {
        method: "PUT",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "id": idTodo,
            "status": true
        })
    });

    const responsePromise = fetch(request);

    responsePromise.then(response => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                console.info("Response 200");
                button.disabled = true;
                button.textContent = "Completed";
                Swal.fire("Success", "", "success");
            } else {
                console.info("Response 400");
                Swal.fire("json.errors", "", "error");
                console.info(json.errors);
            }
        })

}


export function doRegister() {
    console.info("doRegister method terpanggil");
    event.preventDefault();

    const registerUsername = document.getElementById("registerUsername").value;
    const registerPassword = document.getElementById("registerPassword").value;

    const request = new Request("/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            username: registerUsername,
            password: registerPassword
        })
    });

    const response = fetch(request);

    response.then((response) => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                console.info("Response 200")
                Swal.fire({
                    title: "Success Register",
                    icon: "success"
                });
                document.getElementById("registerUsername").value = "";
                document.getElementById("registerPassword").value = "";
                const closeButton = document.getElementById("closeButton");
                closeButton.click();
            } else {
                console.info("Response 400");
                Swal.fire({
                    title: "Failed Register",
                    text: json.errors,
                    icon: "error"
                });
            }
        })
}

export function doLogin() {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    console.info(username);
    console.info(password);

    const request = new Request("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    });

    const response = fetch(request);

    response.then(response => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                window.location = "/home";
            } else {
                Swal.fire({
                    title: "Failed Login",
                    text: json.errors,
                    icon: "error"
                });
            }
        })
}


export function logout() {

    const request = new Request("/api/auth/logout", {
        method: "DELETE",
        headers: {
            "Accept": "application/json"
        }
    });

    const response = fetch(request);

    response.then(response => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                window.location = "/login";
            } else {
                console.info(json.errors);
            }
        });
}

export function getTodos() {
    const request = new Request("/api/users", {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    });

    const responsePromise = fetch(request);

    responsePromise.then((result) => Promise.all([result.status, result.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                fetch(`/api/todos/${json.data.username}`, {
                    method: "GET",
                    headers: {
                        "accept": "application/json"
                    }
                }).then(response => Promise.all([response.status, response.json()]))
                    .then(([statusCode, jsonTodo]) => {
                        if (statusCode == 200) {

                            for (let i = 0; i < jsonTodo.data.length; i++) {
                                const th = document.createElement("th");
                                th.setAttribute("scope", "row");
                                th.textContent = (i + 1);

                                const tdValue = document.createElement("td");
                                tdValue.textContent = jsonTodo.data[i].description;

                                const button = document.createElement("button");
                                button.setAttribute("class", "btn btn-primary btn-lg");
                                button.onclick = () => {
                                    Swal.fire({
                                        title: "Complete Task ?",
                                        icon: "question",
                                        showCancelButton: true,
                                        confirmButtonText: "Complete",
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            updateStatus(jsonTodo.data[i].id, button);
                                            Swal.fire("Completed", "", "success");
                                        }
                                    });
                                };

                                if (jsonTodo.data[i].status == true) {
                                    button.textContent = "Completed";
                                    button.disabled = true;
                                } else {
                                    button.textContent = "Pending";
                                }

                                const tdStatus = document.createElement("td");
                                tdStatus.setAttribute("class", "status");
                                tdStatus.appendChild(button);

                                const a = document.createElement("a");
                                a.setAttribute("href", "javascript:;");
                                a.setAttribute("class", "gg-trash");
                                a.onclick = () => {
                                    Swal.fire({
                                        title: "Delete Task ?",
                                        icon: "warning",
                                        showCancelButton: true,
                                        confirmButtonText: "Delete",
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            deleteTask(jsonTodo.data[i].id);
                                        }
                                    });
                                };

                                const tdA = document.createElement("td");
                                tdA.appendChild(a);

                                const tr = document.createElement("tr");
                                tr.appendChild(th);
                                tr.appendChild(tdValue);
                                tr.appendChild(tdStatus);
                                tr.appendChild(tdA);

                                const tBody = document.getElementById("tr");
                                tBody.appendChild(tr);
                            }
                        }
                    })
            } else {
                console.info(json.errors);
            }
        });
}

function deleteTask(idTodo) {
    const request = new Request(`api/todos/${idTodo}`, {
        method: "DELETE",
        headers: {
            "Accept": "application/json"
        }
    });

    const responsePromise = fetch(request);

    responsePromise.then((response) => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                document.getElementById("tr").textContent = "";
                getTodos();
                Swal.fire("Success", "", "success");
            } else {
                console.info(json.errors);
                Swal.fire(json.errors, "", "error");
            }
        })
}

export function addTask() {
    console.info("addTask terpanggil");
    event.preventDefault();
    const task = document.getElementById("task").value;
    const request = new Request("/api/todos", {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "description": task
        })
    });

    const responsePromise = fetch(request);

    responsePromise.then((response) => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                document.getElementById("task").value = "";
                document.getElementById("tr").textContent = "";
                getTodos();
                Swal.fire("Success", "", "success");
            } else {
                console.info(json.errors);
                Swal.fire(json.errors, "", "error");
            }
        });
}

export function changePassword() {
    event.preventDefault();
    console.info("Terpanggil changepass")
    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const repeatPassword = document.getElementById("repeatPassword").value;

    const request = new Request("/api/users/password", {
        method: "PUT",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "oldPassword": oldPassword,
            "newPassword": newPassword,
            "repeatPassword": repeatPassword
        })
    });

    const responsePromise = fetch(request);

    responsePromise.then((response) => Promise.all([response.status, response.json()]))
        .then(([status, json]) => {
            if (status == 200) {
                Swal.fire("Success", "", "success");

            } else {
                Swal.fire(json.errors, "", "error");
            }
        })

}
