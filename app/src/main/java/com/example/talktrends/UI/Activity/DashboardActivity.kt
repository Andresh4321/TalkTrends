package com.example.talktrends.UI.Activity

import com.example.talktrends.adapter.PostFragmentAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.talktrends.R
import com.example.talktrends.UI.fragment.FriendsFragment
import com.example.talktrends.UI.fragment.HomeFragment

import com.example.talktrends.databinding.ActivityDashboardBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.talktrends.UI.fragment.PopularFragment
import com.example.talktrends.UI.fragment.ProfileFragment
import com.example.talktrends.UI.fragment.SettingFragment

class DashboardActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDashboardBinding

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigate to CreateActivity when create button is clicked
        binding.fabAdd.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment()) // Home tab
                R.id.nav_friends -> loadFragment(FriendsFragment()) // Friends tab
                R.id.nav_profile -> loadFragment(ProfileFragment()) // Profile tab
                R.id.nav_settings -> loadFragment(SettingFragment()) // Settings tab
                else ->  loadFragment(HomeFragment())
            }
            true
        }
        // Check for the Intent extra to determine which tab to select
        val selectedTab = intent.getIntExtra("SELECTED_TAB", R.id.nav_home) // Default to Home tab
        binding.bottomNavigation.selectedItemId = selectedTab // Select the appropriate tab

        // Optionally, load the corresponding fragment directly if needed
        if (selectedTab == R.id.nav_profile) {
            loadFragment(ProfileFragment()) // Force load ProfileFragment if needed
        }
        else if (selectedTab == R.id.nav_friends) {
            loadFragment(FriendsFragment()) // Force load ProfileFragment if needed
        }

    }


private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
        return true
    }
    }


