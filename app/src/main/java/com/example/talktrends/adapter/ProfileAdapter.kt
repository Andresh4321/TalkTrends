package com.example.talktrends.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.UI.Activity.CreateActivity
import com.example.talktrends.UI.Activity.EditProfileActivity
import com.example.talktrends.adapters.PostAdapter
import com.example.talktrends.adapters.PostAdapter.PostViewHolder
import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import kotlin.io.encoding.Base64

class ProfileAdapter( private val profileList: List<UserModel>,
                      private val context: Context
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView = itemView.findViewById(R.id.profileImage)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userBio: TextView = itemView.findViewById(R.id.userBio)
        val editProfileButton: MaterialButton = itemView.findViewById(R.id.editProfileButton)
        val addStoryButton: MaterialButton = itemView.findViewById(R.id.addStoryButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_profile, parent, false) // Use the correct item layout
        return ProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val user = profileList[position]
        holder.userName.text=user.username
        // Set the text content
        holder.userBio.text = user.about

        // Decode the Base64 image string
        if (!user.profile.isNullOrEmpty()) {
            try {
                val decodedBytes = android.util.Base64.decode(user.profile, android.util.Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.profileImage.setImageBitmap(decodedBitmap)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // Show an error placeholder if decoding fails
                holder.profileImage.setImageResource(R.drawable.profile)
            }
        } else {
            // Show a placeholder if the image string is empty or null
            holder.profileImage.setImageResource(R.drawable.profile)
        }


    }
    }



