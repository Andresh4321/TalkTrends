package com.example.talktrends.UI.Activity

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
import com.example.talktrends.R
import com.example.talktrends.Repositary.UserRepository
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.databinding.ActivityLoginBinding
import com.example.talktrends.model.UserModel
import com.example.talktrends.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText

class Login_Activity : AppCompatActivity() {
        lateinit var binding: ActivityLoginBinding
        lateinit var UserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo=UserRepositoryImpl()
        UserViewModel=UserViewModel(repo)



        val signUp = findViewById<TextView>(R.id.singUp)
        val recover=findViewById<TextView>(R.id.recover)


        recover.setOnClickListener {
            val intent = Intent(this, RecoverActivity::class.java)
            startActivity(intent)
        }

        binding.singIn.setOnClickListener {
            var email=binding.eMail.text.toString()
            var password=binding.passwords.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your name and password", Toast.LENGTH_SHORT).show()
            } else {
                UserViewModel.login(email,password){
                        success,message->if(success){
                    val intent=Intent(this@Login_Activity,DashboardActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@Login_Activity,message,Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
                }

            }
        }

        signUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
            }
        }
    }
