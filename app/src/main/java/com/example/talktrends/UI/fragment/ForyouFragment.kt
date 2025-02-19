package com.example.talktrends.UI.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.Repositary.PostRepositoryImpl
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.adapters.PostAdapter
import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel

class ForyouFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foryou, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        postViewModel = PostViewModel(PostRepositoryImpl())
        userViewModel = UserViewModel(UserRepositoryImpl())

        // Fetch the current user's selected genre
        val userId = userViewModel.getCurrentUser()?.uid

        if (userId != null) {
            // Get the user's selected genre
            userViewModel.getSelectedGenre(userId) { selectedGenre, success, message ->
                if (success && selectedGenre != null) {
                    // Fetch all posts
                    postViewModel.getAllPosts { posts, postSuccess, postMessage ->
                        if (postSuccess && posts != null) {
                            // Filter posts based on the selected genre
                            val filteredPosts = posts.filter { it.genre.equals(selectedGenre, ignoreCase = true) }

                            if (filteredPosts.isNotEmpty()) {
                                // Setup the adapter with the filtered posts
                                adapter = PostAdapter(filteredPosts) { post ->
                                    // Handle like updates if needed
                                    postViewModel.updateLikes(post.postId, post.like) { likeSuccess, likeMessage ->
                                        if (likeSuccess) {
                                            Toast.makeText(context, "Like updated!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to update like: $likeMessage", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                                recyclerView.adapter = adapter
                            } else {
                                Toast.makeText(context, "No posts found for this genre", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Failed to load posts: $postMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch genre: $message", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}