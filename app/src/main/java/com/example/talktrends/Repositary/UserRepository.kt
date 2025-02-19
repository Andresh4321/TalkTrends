package com.example.talktrends.Repositary

import android.content.Context
import android.net.Uri
import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    fun login(email:String,password:String,callback: (Boolean, String) -> Unit)

    fun signUp(email:String, password: String, callback:(Boolean,String,String)->Unit)

    fun Selectgenre(genre:String,userId: String,callback: (Boolean, String) -> kotlin.Unit)


    fun updateProfile(userId: String, username:String, contact:String,profileImage: String?, about: String, callback: (Boolean, String) -> Unit)

    fun getProfile(userId: String, callback: (UserModel?, Boolean, String) -> Unit)

    fun updateGenre(userId: String, genre: String, callback: (Boolean, String) -> Unit)

    fun addProfile(userId: String,UserModel: UserModel, callback: (Boolean, String) -> Unit)

    fun addUserToDatabase(userId: String,userModel: UserModel,
                          callback: (Boolean, String) -> Unit)

     fun getUserProfile(userId: String, callback: (UserModel?, Boolean, String) -> Unit)



    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?

    fun getUser(userId: String, callback: (UserModel?, Boolean, String) -> Unit)

    fun forgetPassword(username:String,email: String,callback: (Boolean, String) -> Unit)

    fun getSelectedGenre(userId: String,callback: (String?, Boolean, String?) -> Unit)


    fun getCurrentUser(): FirebaseUser?
}