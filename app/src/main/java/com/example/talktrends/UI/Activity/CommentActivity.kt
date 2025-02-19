package com.example.talktrends.UI.Activity

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.Repositary.CommentRepositaryImpl
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.adapter.CommentAdapter
import com.example.talktrends.model.CommentModel
import com.google.android.material.button.MaterialButton

class CommentActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var postId: String
    private lateinit var commentInput: EditText
    private lateinit var sendButton: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter

    private val userRepository = UserRepositoryImpl() // Create an instance of UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        // Retrieve userId and postId from Intent
        userId = intent.getStringExtra("USER_ID") ?: ""
        postId = intent.getStringExtra("POST_ID") ?: ""

        // Initialize UI elements
        commentInput = findViewById(R.id.etComment)
        sendButton = findViewById(R.id.btnSend)
        recyclerView = findViewById(R.id.rvComments)

        // Set up RecyclerView
        commentAdapter = CommentAdapter(emptyList()) // Initialize with empty list
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = commentAdapter

        // Load comments for the post
        loadComments(postId)

        // Set up send button click listener
        sendButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotEmpty()) {
                submitComment(commentText)
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitComment(commentText: String) {
        // Retrieve user profile information using userId
        userRepository.getUser (userId) { user, success, message ->
            if (success && user != null) {
                // Create a new comment
                val comment = CommentModel(
                    userId = userId,
                    username = user.username, // Use the retrieved username
                    profileimage = user.profile, // Use the retrieved profile image
                    postId = postId,
                    text = commentText
                )

                // Save comment to database
                val commentRepository = CommentRepositaryImpl()
                commentRepository.addComment(comment) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show()
                        commentInput.text.clear() // Clear input field
                        loadComments(postId) // Reload comments
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadComments(postId: String) {
        val commentRepository = CommentRepositaryImpl()
        commentRepository.getCommentByPost(postId) { comments, success, message ->
            if (success && comments != null) {
                commentAdapter.updateComments(comments) // Update the adapter with new comments
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}