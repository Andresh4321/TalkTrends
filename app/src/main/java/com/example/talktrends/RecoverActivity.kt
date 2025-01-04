package com.example.talktrends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.databinding.ActivityRecoverBinding

class RecoverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecoverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle Confirm button click
        binding.button.setOnClickListener {
            val enteredUsername = binding.etUsername.text.toString()
            val enteredEmail = binding.etEmail.text.toString()
            val enteredRecoveryWord = binding.recover.text.toString()
            val newPassword = binding.newPassword.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (enteredUsername.isEmpty() || enteredEmail.isEmpty() || enteredRecoveryWord.isEmpty() ||
                newPassword.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve saved data from SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val savedUsername = sharedPreferences.getString("username", null)
            val savedEmail = sharedPreferences.getString("email", null)
            val savedRecoveryWord = sharedPreferences.getString("recover", null)

            // Validate user inputs
            if (enteredUsername == savedUsername &&
                enteredEmail == savedEmail &&
                enteredRecoveryWord == savedRecoveryWord
            ) {
                // Save new password
                with(sharedPreferences.edit()) {
                    putString("password", newPassword)
                    apply()
                }
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, loginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid details. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Back to Login
        binding.Back.setOnClickListener {
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
