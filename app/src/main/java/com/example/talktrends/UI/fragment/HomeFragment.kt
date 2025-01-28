package com.example.talktrends.UI.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.example.talktrends.R
import com.example.talktrends.UI.Activity.DashboardActivity
import com.example.talktrends.adapter.PostFragmentAdapter
import com.example.talktrends.databinding.ActivityDashboardBinding
import com.example.talktrends.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    lateinit var adapter: PostFragmentAdapter
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!

        // Tab titles for the ViewPager2
        private val tabTitles = arrayOf("Popular", "For You", "Recent")

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Set up the adapter for ViewPager2
            val adapter = PostFragmentAdapter(requireActivity() as DashboardActivity)
            binding.viewPager.adapter = adapter

            // Link TabLayout and ViewPager2
            TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
