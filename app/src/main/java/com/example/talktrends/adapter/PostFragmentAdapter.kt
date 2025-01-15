package com.example.talktrends.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talktrends.UI.Activity.DashboardActivity
import com.example.talktrends.UI.fragment.ForyouFragment
import com.example.talktrends.UI.fragment.PopularFragment
import com.example.talktrends.UI.fragment.RecentFragment

class PostFragmentAdapter(activity: DashboardActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularFragment() // Popular Fragment
            1 -> ForyouFragment() // For You Fragment
            2 -> RecentFragment() // Recent Fragment
            else -> PopularFragment()
        }
    }
}

