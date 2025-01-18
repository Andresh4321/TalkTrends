package com.example.talktrends.viewModel

import com.example.talktrends.Repositary.UserRepository
import com.example.talktrends.model.UserModel
import com.google.firebase.auth.FirebaseUser

class UserViewModel(var repo:UserRepository) {
    fun login(email:String,password:String,callback: (Boolean, String) -> Unit){
        repo.login(email,password,callback)
    }

    fun signUp(email:String, password: String, callback:(Boolean,String,String)->Unit){
        repo.signUp(email,password,callback)
    }

    fun selectGenre(genre: String, userId: String, callback: (Boolean, String) -> Unit) {
        repo.Selectgenre(genre, userId, callback)
    }

    fun updateGenre(userId: String, genre: String, callback: (Boolean, String) -> Unit){
        repo.updateGenre(userId,genre,callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserModel,
                          callback: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun forgetPassword(username:String,email: String,callback: (Boolean, String) -> Unit){
        repo.forgetPassword(username,email,callback)
    }

    fun getCurrentUser(): FirebaseUser?{
        return repo.getCurrentUser()
    }
}