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

    override fun addUserToDatabase(userId: String, userModel: UserModel,
                          callback: (Boolean, String) -> Unit){
        ref.child(userId.toString()).setValue(userModel).addOnCompleteListener {
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

    override fun getCurrentUser(): FirebaseUser? {
        TODO("Not yet implemented")
    }
}