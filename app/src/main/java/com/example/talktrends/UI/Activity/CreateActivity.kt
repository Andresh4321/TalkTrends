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

class CreateActivity : AppCompatActivity() {

    lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    lateinit var imageView: ImageView
    var selectedImageUri: Uri? = null
    lateinit var spinner: Spinner
    lateinit var binding:ActivityCreateBinding
    lateinit var PostViewModel:PostViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var repo = PostRepositoryImpl()
        PostViewModel = PostViewModel(repo)

        // Initialize the ImageView
        imageView = findViewById(R.id.image_view)

        // Set up the Image Picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    // Display the selected image in the ImageView
                    imageView.setImageURI(it)
                    // Store the selected URI for future use (posting)
                    selectedImageUri = it
                }
            }
        }

        // Insert Image Button Click
        val btnInsertImage: Button = findViewById(R.id.btn_insert_image)
        btnInsertImage.setOnClickListener {
            openImagePicker() // Trigger the image picker
        }



        binding.btnPost.setOnClickListener {
            var text = binding.editText.text.toString()
            var image = selectedImageUri

            if (image != null) {
                val bitmap = uriToBitmap(image, this)
                val base64Image = encodeImageToBase64(bitmap)

                var model = PostModel("",text, image = base64Image)

                PostViewModel.addPost(model) { success, message ->
                    if (success) {
                        Toast.makeText(this@CreateActivity, "message", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@CreateActivity, "Post Posted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CreateActivity, "message", Toast.LENGTH_SHORT).show()
                    }

                }
                // Hide the keyboard before posting
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)


                val resultIntent = Intent()
                setResult(RESULT_OK, resultIntent)

                // Return to the dashboard
                finish()
            }


            val categories = arrayOf("Sci-Fi", "Science", "History")
            spinner = findViewById(R.id.spinner)

            val autoAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categories
            )
            autoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = autoAdapter

            // Adjust the dialog size
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )


        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }
}

private fun uriToBitmap(uri: Uri, context: Context): Bitmap {
    return context.contentResolver.openInputStream(uri).use { inputStream ->
        BitmapFactory.decodeStream(inputStream) ?: throw IllegalArgumentException("Cannot decode bitmap")
    }
}

fun encodeImageToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}

