package com.example.talktrends.UI.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.talktrends.R
import com.example.talktrends.UI.Activity.DashboardActivity
import com.example.talktrends.UI.Activity.EditProfileActivity
import com.example.talktrends.UI.Activity.Login_Activity
import com.google.firebase.auth.FirebaseAuth


class SettingFragment : Fragment() {
    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // Set onClick listeners for each LinearLayout
        val profileLayout: LinearLayout = view.findViewById(R.id.profile_section)
        val logoutLayout: LinearLayout = view.findViewById(R.id.logout_section)
        val friendsLayout: LinearLayout = view.findViewById(R.id.friends_section)
        val editLayout:LinearLayout=view.findViewById(R.id.Edit_Profile)

        // Handle click for Profile section
        profileLayout.setOnClickListener {
            // Navigate to DashboardActivity and open ProfileFragment
            val intent = Intent(requireContext(), DashboardActivity::class.java)
            intent.putExtra("SELECTED_TAB", R.id.nav_profile)  // Send signal to open Profile tab
            startActivity(intent)
        }

        // Handle click for Profile section
        friendsLayout.setOnClickListener {
            // Navigate to DashboardActivity and open ProfileFragment
            val intent = Intent(requireContext(), DashboardActivity::class.java)
            intent.putExtra("SELECTED_TAB", R.id.nav_friends)  // Send signal to open Profile tab
            startActivity(intent)
        }

        // Handle click for Profile section
        editLayout.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Handle click for Logout section
        logoutLayout.setOnClickListener {
            // Clear user session or Firebase authentication if applicable
            FirebaseAuth.getInstance().signOut()

            // Navigate to Login Activity
            val intent = Intent(requireContext(), Login_Activity::class.java)
            startActivity(intent)

            // Optionally, finish the current activity to prevent going back to it
            activity?.finish()
        }

        return view
    }
}