// main.js - Simple shared functions

// Theme toggle
function toggleTheme() {
  const body = document.body;
  const currentTheme = body.getAttribute("data-theme");
  const newTheme = currentTheme === "dark" ? "light" : "dark";

  body.setAttribute("data-theme", newTheme);
  localStorage.setItem("theme", newTheme);

  const themeIcon = document.querySelector(".theme-toggle i");
  if (themeIcon) {
    themeIcon.className = newTheme === "dark" ? "fas fa-sun" : "fas fa-moon";
  }
}

// Initialize theme
function initTheme() {
  const savedTheme = localStorage.getItem("theme") || "light";
  document.body.setAttribute("data-theme", savedTheme);

  const themeIcon = document.querySelector(".theme-toggle i");
  if (themeIcon) {
    themeIcon.className = savedTheme === "dark" ? "fas fa-sun" : "fas fa-moon";
  }
}

// Simple API GET
async function apiGet(url) {
  const response = await fetch(url, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
  });

  if (!response.ok) throw new Error('Request failed');
  return await response.json();
}

// Simple API POST
async function apiPost(url, data) {
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) throw new Error('Request failed');
  return await response.json();
}

// Simple API DELETE
async function apiDelete(url) {
  const response = await fetch(url, {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' }
  });

  if (!response.ok) throw new Error('Request failed');
  return true;
}

// Simple API PUT
async function apiPut(url, data) {
  const response = await fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) throw new Error('Request failed');
  return await response.json();
}

// Show toast notification
function showToast(message, type = "info") {
  const toast = document.getElementById("toast");
  if (!toast) return;

  toast.textContent = message;
  toast.className = `toast ${type} show`;

  setTimeout(() => {
    toast.classList.remove("show");
  }, 3000);
}

// Navbar placeholder for future authentication integration
function updateNavbar() {
  return;
}

// Logout placeholder for future authentication integration
async function logout() {
  sessionStorage.clear();
}

// Check if user is admin
function isAdmin() {
  return true;
}

// Hide elements for non-admin users
function applyRoleBasedUI() {
  if (!isAdmin()) {
    // Hide admin-only elements
    document.querySelectorAll('.admin-only').forEach(el => {
      el.style.display = 'none';
    });
  }
}
