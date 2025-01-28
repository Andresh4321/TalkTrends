package com.example.talktrends.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.UI.Activity.EditProfileActivity
import com.example.talktrends.model.UserModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView

class ProfileAdapter(private val userList: List<UserModel>, private val onProfileClick: (UserModel) -> Unit) :
        RecyclerView.Adapter<ProfileAdapter.UserViewHolder>() {

        // ViewHolder to hold the views for each item
        class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val profileImage: CircleImageView = view.findViewById(R.id.profileImage)
            val userName: TextView = view.findViewById(R.id.userName)
            val userBio: TextView = view.findViewById(R.id.userBio)
            val editProfileButton: MaterialButton = view.findViewById(R.id.editProfileButton)
            val addStoryButton: MaterialButton = view.findViewById(R.id.addStoryButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_profile, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = userList[position]
            holder.userName.text = user.username
            holder.userBio.text = user.address


            holder.profileImage.setImageResource(R.drawable.profile)


            holder.editProfileButton.setOnClickListener {
                val intent = Intent(holder.itemView.context, EditProfileActivity::class.java)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }


            holder.addStoryButton.setOnClickListener {

            }


        }

        override fun getItemCount(): Int {
            return userList.size
        }
    }