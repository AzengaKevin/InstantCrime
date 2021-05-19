package com.students.instantcrime.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.enums.Role
import com.students.instantcrime.data.models.User
import com.students.instantcrime.databinding.ActivityRegisterBinding
import com.students.instantcrime.helpers.hide
import com.students.instantcrime.helpers.show
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.HomeActivity
import java.util.*

private const val TAG = "RegisterActivity"

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginTextView.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener { handleUserRegistration() }
    }

    private fun handleUserRegistration() {

        val name = binding.nameField.text.toString()
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if (name.isEmpty()) {
            binding.nameField.error = "Name is required"
            binding.nameField.requestFocus()
            return
        }

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

        binding.registerProgressBar.show()

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                binding.registerProgressBar.hide()

                if (it.isSuccessful) {
                    toast("You've successfully register in")

                    setUpProfile(name);

                } else {
                    binding.registerProgressBar.hide()

                    Log.e(TAG, "handleUserRegistration: ", it.exception)

                    toast("An error occurred during authentication, check the logs fro more intel")
                }
            }
    }

    private fun setUpProfile(name: String) {

        Firebase.firestore.collection(Constants.USERS_ROOT)
            .document(Firebase.auth.currentUser!!.uid)
            .set(
                User(
                    name = name,
                    role = Role.Default.toString(),
                    joinDate = Date()
                )
            )
            .addOnCompleteListener {

                binding.registerProgressBar.hide()

                if (it.isSuccessful) {

                    toast("Successful profile setup")

                } else {
                    toast("You'll have to update your profile later")
                }

                sendHome()
            }


    }

    private fun sendHome() {

        Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also { startActivity(it) }

    }
}