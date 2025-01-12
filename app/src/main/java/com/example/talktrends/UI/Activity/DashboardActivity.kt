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
import com.example.talktrends.R

import com.example.talktrends.databinding.ActivityDashboardBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.talktrends.UI.fragment.PopularFragment

class DashboardActivity : AppCompatActivity() {

    lateinit var adapter: PostFragmentAdapter
    private lateinit var binding: ActivityDashboardBinding

    var data = arrayOf("Popular", "For You", "Recent")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // When CreateActivity finishes, update PopularFragment
            val popularFragment = supportFragmentManager.findFragmentByTag("popular") as? PopularFragment
            popularFragment?.updatePosts()  // Method to update the fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PostFragmentAdapter(this)
        binding.view.adapter = adapter // Set the adapter for the ViewPager2

        // Setup tabs
        TabLayoutMediator(binding.tab, binding.view) { tabs, position ->
            tabs.text = data[position]
        }.attach()

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


    }
}
