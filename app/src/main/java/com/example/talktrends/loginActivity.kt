package com.example.talktrends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_login)

        val emailEditText: TextInputEditText = findViewById(R.id.eMail)
        val passwordEditText: TextInputEditText = findViewById(R.id.passwords)
        val loginButton: Button = findViewById(R.id.singIn)
        val signUp = findViewById<TextView>(com.example.talktrends.R.id.singUp)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        signUp.setOnClickListener { val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val name = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your name and password", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val registeredUsername = sharedPreferences.getString("username", null)
                val registeredPassword = sharedPreferences.getString("password", null)

                if (name == registeredUsername && password == registeredPassword) {
                    val intent = Intent(this,DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
}
