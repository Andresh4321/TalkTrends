package com.example.talktrends.UI.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.Repositary.PostRepositoryImpl
import com.example.talktrends.adapters.PostAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.UI.Activity.CreateActivity
import com.example.talktrends.UI.Activity.EditProfileActivity

import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel
import com.example.talktrends.viewModel.UserViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {private lateinit var userViewModel: UserViewModel
     lateinit var editProfileButton: com.google.android.material.button.MaterialButton
     lateinit var addStoryButton: com.google.android.material.button.MaterialButton
     lateinit var postViewModel: PostViewModel
     lateinit var postAdapter: PostAdapter
     lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize ViewModel
        val repository = UserRepositoryImpl()
        val viewModelFactory = UserViewModelFactory(repository)
        val repo=PostRepositoryImpl()
        postViewModel = PostViewModel(repo)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        // Initialize views
        val profileImage: CircleImageView = view.findViewById(R.id.profileImage)
        val userName: TextView = view.findViewById(R.id.userName)
        val userBio: TextView = view.findViewById(R.id.userBio)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        addStoryButton=view.findViewById(R.id.addStoryButton)

        // Set click listener for Edit Profile button
        editProfileButton.setOnClickListener {
            navigateToEditProfile()
        }

        addStoryButton.setOnClickListener {
            navigateToCreate()
        }




        // Load user data
        val userId = userViewModel.getCurrentUser()?.uid
        if (userId != null) {
            userViewModel.getUserProfile(userId)
        } else {
            showLoginPrompt()
        }

        // Observe user profile data
        userViewModel.userProfile.observe(viewLifecycleOwner) { user ->
            user?.let {
                userName.text = it.username
                userBio.text = it.about
                loadProfileImage(profileImage, it.profile)
            }
        }
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.viewPager)
        recyclerView.layoutManager = LinearLayoutManager(context)
        postAdapter = PostAdapter(emptyList()) { post ->
            // Handle like button click
        }
        recyclerView.adapter = postAdapter

        // Load user data
        if (userId != null) {
            userViewModel.getUserProfile(userId)
            loadUserPosts(userId) // Load posts for the user
        } else {
            showLoginPrompt()
        }

        // Observe user profile data
        userViewModel.userProfile.observe(viewLifecycleOwner) { user ->
            user?.let {
                userName.text = it.username
                userBio.text = it.about
                loadProfileImage(profileImage, it.profile)
            }
        }

        return view
    }


    private fun loadUserPosts(userId: String) {
        postViewModel.getPostsByUser(userId) { posts, success, message ->
            if (success) {
                // Use the Elvis operator to provide an empty list if posts is null
                val nonNullPosts = posts ?: emptyList()
                postAdapter = PostAdapter(nonNullPosts) { post ->
                    // Handle like button click
                }
                recyclerView.adapter = postAdapter
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToEditProfile() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCreate() {
        val intent = Intent(requireContext(), CreateActivity::class.java)
        startActivity(intent)
    }

    private fun loadProfileImage(imageView: CircleImageView, profileImageString: String?) {
        if (!profileImageString.isNullOrEmpty()) {
            val bitmap = profileImageString.base64ToBitmap()
            bitmap?.let {
                imageView.setImageBitmap(it)
            } ?: imageView.setImageResource(R.drawable.profile)
        } else {
            imageView.setImageResource(R.drawable.profile)
        }
    }

    private fun showLoginPrompt() {
        Toast.makeText(requireContext(), "Please login to view profile", Toast.LENGTH_SHORT).show()
    }

    // Extension function to convert Base64 string to Bitmap
    private fun String.base64ToBitmap(): Bitmap? {
        return try {
            val pureBase64 = this.substringAfterLast(",")
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
