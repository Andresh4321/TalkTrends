package com.example.talktrends.UI.Activity

import com.example.talktrends.Repositary.PostRepository
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.talktrends.R
import com.example.talktrends.model.Post

class CreateActivity : AppCompatActivity() {

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null
    lateinit var spinner: Spinner








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)

        val btnPost: Button = findViewById(R.id.btn_post)
        val editText: EditText = findViewById(R.id.edit_text)


            btnPost.setOnClickListener {
                // Hide the keyboard before posting
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                // Post handling code
                val text = editText.text.toString()
                val imageUri = selectedImageUri
                PostRepository.posts.add(Post(text, imageUri))

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

        // Initialize views
        val btnInsertImage: Button = findViewById(R.id.btn_insert_image)
        imageView = findViewById(R.id.image_view)

        // Set up the Image Picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    // Display the selected image in the ImageView
                    imageView.setImageURI(it)
                }
            }
        }

        // Handle button click
        btnInsertImage.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }
}
