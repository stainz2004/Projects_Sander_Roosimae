import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-app.js";
import { getAuth, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-auth.js";

// Firebase Configuration
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
const auth = getAuth(app);

// Submit button event listener for login
const submit = document.getElementById("login");
submit.addEventListener("click", function (event) {
  event.preventDefault();

  // Get input values for email and password
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  // Sign in the user with the provided credentials
  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      const user = userCredential.user;
      // Save user email to localStorage
      localStorage.setItem("userEmail", user.email);
      window.location.href = "index.html"; // Redirect to another page
    })
    .catch((error) => {
      // Handle errors and show error message
      const customErrorMessage = "An error occurred. Please check your details and try again.";
      alert(customErrorMessage);
    });
});