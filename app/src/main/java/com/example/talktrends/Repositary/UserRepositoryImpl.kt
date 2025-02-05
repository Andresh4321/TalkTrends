package com.example.talktrends.Repositary

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

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
        ref.child(userId).setValue(UserModel).addOnCompleteListener(){
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

    override fun getUserProfile(userId: String, callback: (UserModel?, Boolean, String) -> Unit) {
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserModel::class.java)
                    callback(user, true, "User profile fetched successfully")
                } else {
                    callback(null, false, "User profile not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Error fetching user profile: ${error.message}")
            }
        })
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

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dmp3qsyet",
            "api_key" to "828334985489613",
            "api_secret" to "lmfK8_pg0v7-wJ_-l5e1jtKCvY0"
        )
    )

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }


    override fun getCurrentUser(): FirebaseUser? {

            return FirebaseAuth.getInstance().currentUser
        }

    }
