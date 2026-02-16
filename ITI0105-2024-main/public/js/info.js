window.addEventListener("DOMContentLoaded", () => {
  // Retrieve the email from localStorage
  const userEmail = localStorage.getItem("userEmail");

  // Check if the email exists, and set the input value
  if (userEmail) {
    // Set the value of the input field with the user's email
    document.getElementById("emailInput").value = userEmail;
  } else {
    // If no email found, show a default message or error
    document.getElementById("emailInput").value = "Perhaps log in?";
  }
});

onAuthStateChanged(auth, (user) => {
  const emailInput = document.getElementById("emailInput");

  if (user) {
    emailInput.value = user.email;
    localStorage.setItem("userEmail", user.email); // Store email in localStorage
  } else {
    emailInput.value = "Perhaps log in?";
    localStorage.removeItem("userEmail"); // Clear localStorage when user logs out
  }
});