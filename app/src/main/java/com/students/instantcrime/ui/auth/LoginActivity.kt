package com.students.instantcrime.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.databinding.ActivityLoginBinding
import com.students.instantcrime.helpers.hide
import com.students.instantcrime.helpers.show
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.HomeActivity

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener { handleUserLogin() }
    }

    private fun handleUserLogin() {

        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if (email.isEmpty()) {
            binding.emailField.error = "Email is required"
            binding.emailField.requestFocus()
            return
        }

        if (password.isEmpty() || password.length < 6) {
            binding.passwordField.error = "Password is required"
            binding.passwordField.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailField.error = "Valid email is required"
            binding.emailField.requestFocus()
            return

        }

        binding.loginProgressBar.show()

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                binding.loginProgressBar.hide()

                if (it.isSuccessful) {
                    toast("You've successfully logged in")
                    sendHome()
                } else {
                    Log.e(TAG, "handleUserRegistration: ", it.exception)
                    toast("An error occurred during authentication, check the logs fro more intel")
                }
            }

    }

    private fun sendHome() {

        Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also { startActivity(it) }

    }

    override fun onStart() {

        super.onStart()

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) finish()

    }

}