document.getElementById("user-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const user = {
    name: document.getElementById("name").value,
    email: document.getElementById("email").value,
    about: document.getElementById("about").value,
  };

  try {
    await apiPost("/api/users", user);
    showAlert("✅ User created successfully!", "success");
    setTimeout(() => (window.location.href = "users.html"), 1500);
  } catch (error) {
    showAlert("❌ Failed to create user. " + error.message, "error");
  }
});

function showAlert(msg, type) {
  const alertContainer = document.getElementById("alert-container");
  alertContainer.innerHTML = `<div class="alert ${type}">${msg}</div>`;
}
