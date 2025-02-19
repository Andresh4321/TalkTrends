package com.example.talktrends.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.talktrends.R
import com.example.talktrends.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.adapters.PostAdapter
import com.example.talktrends.databinding.ActivityProfile2Binding

class ProfileActivity : AppCompatActivity() {

    private lateinit var userId: String


    private lateinit var userNameTextView: TextView
    private lateinit var userBioTextView: TextView
    private lateinit var profileImageView: CircleImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

            override fun onCreate(savedInstanceState: Bundle?) {
                val binding:ActivityProfile2Binding
                super.onCreate(savedInstanceState)

                binding=ActivityProfile2Binding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.back.setOnClickListener {
                    val intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
                    startActivity(intent)
                }

                binding.follow.setOnClickListener{


                }

                // Retrieve userId from Intent
                userId = intent.getStringExtra("USER_ID") ?: ""

                // Initialize UI elements
                userNameTextView = findViewById(R.id.userName)
                userBioTextView = findViewById(R.id.userBio)
                profileImageView = findViewById(R.id.profileImage)
                recyclerView = findViewById(R.id.viewPager) // Ensure this ID matches your layout

                // Initialize RecyclerView
                postAdapter = PostAdapter(emptyList()) { post -> /* Handle like click */ }
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = postAdapter

                // Load user data
                loadUserData(userId)
                // Load user posts
                loadUserPosts(userId)
            }

            private fun loadUserData(userId: String) {
                val userRepository = UserRepositoryImpl()
                userRepository.getUser (userId) { user, success, message ->
                    if (success && user != null) {
                        // Set user data to UI
                        userNameTextView.text = user.username
                        userBioTextView.text = user.about
                        loadProfileImage(user.profile)
                    } else {
                        // Handle error (e.g., show a Toast)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

             fun loadProfileImage(imageString: String?) {
                if (!imageString.isNullOrEmpty()) {
                    try {
                        val decodedBytes = Base64.decode(imageString, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        profileImageView.setImageBitmap(decodedBitmap)
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        profileImageView.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image
                    }
                } else {
                    profileImageView.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image
                }
            }

             fun loadUserPosts(userId: String) {
                val database = FirebaseDatabase.getInstance()
                val postsRef = database.reference.child("Posts")

                postsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val posts = mutableListOf<PostModel>()
                            for (postSnapshot in snapshot.children) {
                                val post = postSnapshot.getValue(PostModel::class.java)
                                if (post != null) {
                                    posts.add(post)
                                }
                            }
                            // Update the RecyclerView with the fetched posts
                            postAdapter = PostAdapter(posts) { post -> /* Handle like click */ }
                            recyclerView.adapter = postAdapter
                        } else {
                            // Handle case where no posts are found
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
            }
        }