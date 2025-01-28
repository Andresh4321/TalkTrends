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
import com.example.talktrends.viewModel.PostViewModel
import com.example.talktrends.viewModel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment (activity_profile.xml)
        return inflater.inflate(R.layout.activity_profile, container, false)
    }
}