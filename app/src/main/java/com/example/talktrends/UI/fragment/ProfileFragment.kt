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
import com.example.talktrends.adapter.ProfileAdapter
import com.example.talktrends.adapters.PostAdapter
import androidx.lifecycle.ViewModelProvider

import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Fetch current user ID (Assuming it comes from Firebase Auth)
        val userId = userViewModel.getCurrentUser()?.uid

        // Observe LiveData for updates
        userViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            if (userProfile != null) {
                adapter = ProfileAdapter(listOf(userProfile), requireContext())
                recyclerView.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        }

        // If userId is not null, fetch user profile (assuming it's in ViewModel logic)
        if (userId != null) {
            userViewModel.getUserProfile(userId)
        }

        return view
    }
}