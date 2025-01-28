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
import com.example.talktrends.Repositary.PostRepository
import com.example.talktrends.Repositary.PostRepositoryImpl
import com.example.talktrends.adapters.PostAdapter
import com.example.talktrends.model.PostModel
import com.example.talktrends.viewModel.PostViewModel

class PopularFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_popular, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        postViewModel = PostViewModel(PostRepositoryImpl())

        // Fetch and display posts
        postViewModel.getAllPosts { posts, success, message ->
            if (success && posts != null) {
                // Sort the posts by likes in descending order
                val sortedPosts = posts.sortedByDescending { it.like }

                // Initialize the adapter with a callback for updating likes
                adapter = PostAdapter(sortedPosts) { post ->
                    // Update the likes in the database
                    postViewModel.updateLikes(post.postId, post.like) { success, updateMessage ->
                        if (success) {
                            Toast.makeText(context, "Like updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to update like: $updateMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                recyclerView.adapter = adapter
            } else {
                // Handle error
                Toast.makeText(context, "Failed to load posts: $message", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}