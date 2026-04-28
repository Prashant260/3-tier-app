// auth.js - Session Authentication Helper
async function syncCurrentUser() {
  const response = await apiCall("https://bloghub-application.onrender.com/api/auth/me");
  if (!response) return;

  const data = await response.json();

  sessionStorage.setItem("userId", data.userId);
  sessionStorage.setItem("userName", data.userName);
  sessionStorage.setItem("userEmail", data.userEmail);
  sessionStorage.setItem("userRole", data.userRole);
}

// Check if user is logged in
function isLoggedIn() {
  return sessionStorage.getItem("userId") !== null;
}

// Get current user info
function getCurrentUser() {
  return {
    userId: sessionStorage.getItem("userId"),
    userName: sessionStorage.getItem("userName"),
    email: sessionStorage.getItem("userEmail"),
    role: sessionStorage.getItem("userRole"),
  };
}

// API Call Wrapper - Sabse Important Function
async function apiCall(url, options = {}) {
  // Default headers setup
  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  const config = {
    ...options,
    headers: headers,
    credentials: "include", // <--- YAHI MAGIC HAI: Ye JSESSIONID cookie backend ko bhejta hai
  };

  try {
    const response = await fetch(url, config);

    // Agar user logged in nahi hai (Session expire ho gaya)
    if (response.status === 401 || response.status === 403) {
      sessionStorage.clear();
      window.location.href = "login.html";
      return null;
    }

    return response;
  } catch (error) {
    console.error("API Error:", error);
    throw error;
  }
}

// Logout function
async function logout() {
  await apiCall("https://bloghub-application.onrender.com/api/auth/logout", { method: "POST" });
  sessionStorage.clear();
  window.location.href = "login.html";
}
