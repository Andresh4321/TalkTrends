package com.example.talktrends.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.R
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.databinding.ActivityCatBinding
import com.example.talktrends.model.UserModel
import com.example.talktrends.viewModel.UserViewModel

class CatActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var userViewModel: UserViewModel
    lateinit var binding: ActivityCatBinding

    var selectedGenre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use your app's package R reference

        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cat)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        button = findViewById(R.id.selectButton)
        // Get all CardViews (correct type is CardView)
        val cardViews = arrayOf<CardView>(
            findViewById(R.id.scifiCard),
            findViewById(R.id.mysteryCard),
            findViewById(R.id.fantasyCard),
            findViewById(R.id.comedyCard),
            findViewById(R.id.scienceCard),
        )


        for (cardView in cardViews) {
            cardView.setOnClickListener {
                // Deselect all card views
                for (card in cardViews) {
                    card.isSelected = false
                    card.setCardBackgroundColor(
                        resources.getColor(
                            R.color.lavender,
                            theme

                        )
                    ) // Reset to default color
                }
                // Highlight the selected card
                cardView.isSelected = true
                cardView.setCardBackgroundColor(
                    resources.getColor(
                        R.color.pinkColor,
                        theme
                    )
                ) // Highlight color
            }
        }
        button.setOnClickListener {
            // Define the genres
            val genres = mapOf(
                R.id.scifiCard to "Sci-Fi",
                R.id.mysteryCard to "Mystery",
                R.id.fantasyCard to "Fantasy",
                R.id.comedyCard to "Comedy",
                R.id.scienceCard to "Science"
            )

            // Get the selected genre
            var selectedGenre: String? = null
            for ((cardId, genre) in genres) {
                val cardView = findViewById<CardView>(cardId)
                if (cardView.isSelected) {
                    selectedGenre = genre
                    break
                }
            }

            if (selectedGenre == null) {
                Toast.makeText(this@CatActivity, "Please select a genre", Toast.LENGTH_SHORT).show()
            } else {
                // Get the current user ID (assume this is passed from a previous activity or retrieved from auth)
                val userId = userViewModel.getCurrentUser()?.uid

                if (userId != null) {
                    // Save the genre to the database
                    userViewModel.selectGenre(selectedGenre, userId) { success, message ->
                        if (success) {
                            Toast.makeText(this@CatActivity, "Genre saved successfully", Toast.LENGTH_SHORT).show()
                            // Navigate to another activity, e.g., LoginActivity
                            val intent = Intent(this@CatActivity, Login_Activity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@CatActivity, "Failed to save genre: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@CatActivity, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}


