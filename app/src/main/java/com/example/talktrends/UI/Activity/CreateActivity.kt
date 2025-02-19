package com.example.talktrends.UI.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.talktrends.R
import com.example.talktrends.Repositary.PostRepositoryImpl
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.databinding.ActivityCreateBinding
import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel
import java.io.ByteArrayOutputStream
import android.util.Base64

class CreateActivity : AppCompatActivity() {

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageView: ImageView
    private lateinit var spinner: Spinner
    private lateinit var binding: ActivityCreateBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupViewModels()
        setupImagePicker()
        setupSpinner()
        checkUserAuthentication()
    }

    private fun initializeViews() {
        imageView = findViewById(R.id.image_view)
        spinner = findViewById(R.id.spinner)

        binding.btnInsertImage.setOnClickListener {
            openImagePicker()
        }
    }

    private fun setupViewModels() {
        val postRepo = PostRepositoryImpl()
        val userRepo = UserRepositoryImpl()
        postViewModel = PostViewModel(postRepo)
        userViewModel = UserViewModel(userRepo)
    }

    private fun checkUserAuthentication() {
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.uid?.let { userId ->
            userViewModel.getUserProfile(userId)
            userViewModel.userProfile.observe(this) { user ->
                user?.let { setupPostCreation(it) } ?: showUserDataError()
            }
        } ?: showLoginError()
    }

    private fun setupPostCreation(user: UserModel) {
        binding.btnPost.setOnClickListener {
            val text = binding.editText.text.toString().trim()
            val genre = spinner.selectedItem?.toString() ?: ""

            if (text.isBlank()) {
                showToast("Please enter text")
                return@setOnClickListener
            }

            val base64Image = selectedImageUri?.let { uri ->
                try {
                    val bitmap = uriToBitmap(uri, this)
                    encodeImageToBase64(bitmap)
                } catch (e: Exception) {
                    showToast("Error processing image")
                    null
                }
            }

            createPost(user, text, genre, base64Image)
        }
    }

    private fun createPost(user: UserModel, text: String, genre: String, image: String?) {
        val post = PostModel(
            userId = user.userId,
            username = user.username,
            profileimage = user.profile,
            text = text,
            image = image,
            genre = genre,
            like = 0
        )

        postViewModel.addPost(post) { success, message ->
            if (success) {
                handlePostSuccess()
            } else {
                handlePostError(message)
            }
        }
    }

    private fun handlePostSuccess() {
        hideKeyboard()
        showToast("Post created successfully!")
        finish()
    }

    private fun handlePostError(message: String?) {
        showToast("Error: ${message ?: "Unknown error"}")
    }

    private fun setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    imageView.setImageURI(uri)
                }
            }
        }
    }

    private fun setupSpinner() {
        val categories = arrayOf("Sci-Fi", "Mystery", "Fantasy", "Comedy", "Science")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = adapter
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoginError() {
        showToast("User not logged in")
        finish()
    }

    private fun showUserDataError() {
        showToast("User data not found")
        finish()
    }
}

// Extension functions outside class
private fun uriToBitmap(uri: Uri, context: Context): Bitmap {
    return context.contentResolver.openInputStream(uri).use { inputStream ->
        BitmapFactory.decodeStream(inputStream) ?: throw IllegalArgumentException("Cannot decode bitmap")
    }
}

private fun encodeImageToBase64(bitmap: Bitmap): String {
    return ByteArrayOutputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}