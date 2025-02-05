package com.example.talktrends.UI.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.R
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.UI.fragment.ProfileFragment
import com.example.talktrends.databinding.ActivityCreateBinding
import com.example.talktrends.databinding.ActivityEditProfileBinding
import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {


    lateinit var binding:ActivityEditProfileBinding
    lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    lateinit var imageView: ImageView
    var selectedImageUri: Uri? = null
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo=UserRepositoryImpl()
        userViewModel= UserViewModel(repo)

        var userId : String = userViewModel.getCurrentUser()?.uid.toString()

        imageView = findViewById(R.id.editProfileImage)

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
        val changeImageButton: Button = findViewById(R.id.changeImageButton)
        changeImageButton.setOnClickListener {
            openImagePicker() // Trigger the image picker
        }


        binding.btnSave.setOnClickListener{
            var text = binding.aboutEditText.text.toString().trim()
            var image=selectedImageUri

            if (image != null) {
                val bitmap = uriToBitmap(image, this)
                val base64Image = ImageProfile(bitmap)

                val model = UserModel(
                    about = text,   // Updating only 'about' field
                    profile = base64Image  // Updating only 'profile' field
                )
                userViewModel.addProfile(userId,model) { success, message ->
                    if (success) {
                        Toast.makeText(this@EditProfileActivity, "message", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@EditProfileActivity, "Post Posted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditProfileActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@EditProfileActivity, "message", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }



                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                    insets
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

   fun ImageProfile(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}
