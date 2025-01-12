package com.example.talktrends.UI.fragment

import com.example.talktrends.Repositary.PostRepository
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.adapters.PostAdapter

class PopularFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    // Add a method to update the posts when new data is added
    fun updatePosts() {
        adapter.notifyDataSetChanged() // Refresh the RecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_popular, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter and set it to the RecyclerView
        adapter = PostAdapter(PostRepository.posts)
        recyclerView.adapter = adapter  // Ensure the adapter is set before layout is applied

        return view
    }
}
