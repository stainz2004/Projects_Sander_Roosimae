// Import Firebase SDKs
import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-app.js";
import { getAuth, createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-auth.js";

// Your Firebase configuration
const firebaseConfig = {
  apiKey: "",
  authDomain: "",
  projectId: "",
  storageBucket: "",
  messagingSenderId: "",
  appId: "",
  measurementId: ""
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app); // Initialize Firebase Auth

// Submit button event listener
const submit = document.getElementById("register");
submit.addEventListener("click", function (event) {
  event.preventDefault();

  // Get input values for email and password
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  // Create a new user with the provided email and password
  createUserWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      const userEmail = userCredential.user.email;
      localStorage.setItem("userEmail", userEmail); // Store email in localStorage
      window.location.href = "index.html"; // Redirect to another page
    })
    .catch((error) => {
      // Handle errors and show error message
      const customErrorMessage = "An error occurred. Please check your details and try again.";
      alert(customErrorMessage);
    });
});