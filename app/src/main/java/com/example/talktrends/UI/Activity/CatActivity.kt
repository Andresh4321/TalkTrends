package com.example.talktrends.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.R

class CatActivity : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat) // Use your app's package R reference

        // Handle edge-to-edge UI insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cat)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        button=findViewById(R.id.selectButton)
        // Get all CardViews (correct type is CardView)
        val cardViews = arrayOf<CardView>(
            findViewById(R.id.scifiCard),
            findViewById(R.id.mysteryCard),
            findViewById(R.id.fantasyCard),
            findViewById(R.id.comedyCard),
            findViewById(R.id.scienceCard),
        )

        // Implement the click listener to ensure only one card is selected
        for (cardView in cardViews) {
            cardView.setOnClickListener {
                // Deselect all card views
                for (card in cardViews) {
                    card.isSelected = false
                    card.setCardBackgroundColor(resources.getColor(R.color.lavender, theme)) // Reset to default color
                }
                // Highlight the selected card
                cardView.isSelected = true
                cardView.setCardBackgroundColor(resources.getColor(R.color.pinkColor, theme)) // Highlight color
            }
        }
        button.setOnClickListener {
            // Define the genres
            val genres = arrayListOf("Sci-Fi", "Mystery", "Fantasy", "Comedy", "Science")

            // Pass the genres list to CreateActivity
            val intent = Intent(this, Login_Activity::class.java)
            intent.putStringArrayListExtra("GENRES_LIST", genres)
            startActivity(intent)
        }

    }
}
