package com.example.talktrends.Repositary

import com.example.talktrends.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    fun login(email:String,password:String,callback: (Boolean, String) -> Unit)

    fun signUp(email:String, password: String, callback:(Boolean,String,String)->Unit)

    fun addUserToDatabase(userId: String,userModel: UserModel,
                          callback: (Boolean, String) -> Unit)

    fun forgetPassword(username:String,email: String,callback: (Boolean, String) -> Unit)


    fun getCurrentUser(): FirebaseUser?
}