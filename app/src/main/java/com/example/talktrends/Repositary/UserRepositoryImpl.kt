package com.example.talktrends.Repositary

import com.example.talktrends.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl: UserRepository {

    lateinit var auth: FirebaseAuth


    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var ref: DatabaseReference = database.reference.child("users")

    init {
        auth = FirebaseAuth.getInstance()
    }

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener({
            if (it.isSuccessful) {
                callback(true, "Registration success")
            } else {
                callback(false, it.exception?.message.toString())

            }
        })
    }

    override fun signUp(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful) {
                callback(true, "Sign up success", auth.currentUser?.uid.toString())
            } else {
                callback(false, it.exception?.message.toString(), "")
            }
        }
    }

    override fun Selectgenre(genre:String,userId: String,callback: (Boolean, String) -> kotlin.Unit){
        ref.child(userId).child("genre").setValue(genre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Genre successfully saved")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }



    override fun updateGenre(userId: String, genre: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("genre").setValue(genre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Genre updated successfully")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }

    override fun addProfile(
        userId: String,
        UserModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).child("users").setValue(UserModel).addOnCompleteListener(){
            if (it.isSuccessful){
                callback(true,"Success")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun addUserToDatabase(userId: String, userModel: UserModel,
                          callback: (Boolean, String) -> Unit){
        ref.child(userId).setValue(userModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Register success")
            } else {
                callback(false,it.exception?.message.toString())
            }
        }

    }

    override fun forgetPassword(username:String,email: String,callback: (Boolean, String) -> Unit){
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "reset email to $email")
            } else {
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun getSelectedGenre(
        userId: String,
        callback: (String?, Boolean, String?) -> Unit
    ) {
        ref.child(userId).child("genre").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val genre = task.result?.getValue(String::class.java) // Retrieve the genre as a String
                callback(genre, true, null) // Pass the genre and success status to the callback
            } else {
                callback(null, false, task.exception?.message) // Pass the error message in case of failure
            }
        }
    }


    override fun getCurrentUser(): FirebaseUser? {

            return FirebaseAuth.getInstance().currentUser
        }

    }
