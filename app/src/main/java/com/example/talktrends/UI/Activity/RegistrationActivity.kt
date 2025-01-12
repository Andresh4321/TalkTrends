package com.example.talktrends.UI.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.R
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.databinding.ActivityRegistrationBinding
import com.example.talktrends.model.UserModel
import com.example.talktrends.viewModel.UserViewModel

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding
    lateinit var UserViewModel:UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo= UserRepositoryImpl()
        UserViewModel=UserViewModel(repo)

        val termsCheckBox: CheckBox = findViewById(R.id.cbTerms)


        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
        }


        binding.btnRegister.setOnClickListener {
            var email = binding.email.text.toString()
            var password = binding.etPassword.text.toString()
            var username = binding.etUsername.text.toString()
            var address = binding.etAddress.text.toString()
            var contact = binding.etContact.text.toString()
            val termsAccepted = termsCheckBox.isChecked

            if (username.isEmpty() || address.isEmpty() || contact.isEmpty()|| email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (!termsAccepted) {
                Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            } else {
                UserViewModel.signUp(email,password){
                        success,message,userId ->
                    if(success){
                        var userModel=UserModel(
                            userId.toString(),
                            username,address,contact,email
                        )
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, CatActivity::class.java) // Change to RegistrationActivity if needed
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@RegistrationActivity,message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}