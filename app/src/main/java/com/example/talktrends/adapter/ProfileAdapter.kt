//package com.example.talktrends.adapter
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.content.ContextCompat.startActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.example.talktrends.R
//import com.example.talktrends.UI.Activity.CreateActivity
//import com.example.talktrends.UI.Activity.EditProfileActivity
//import com.example.talktrends.model.UserModel
//import com.google.android.material.button.MaterialButton
//import de.hdodenhof.circleimageview.CircleImageView
//import android.util.Base64
//
//class ProfileAdapter(
//    private val profileList: List<UserModel>,
//    private val context: Context
//) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
//
//    // Fix 1: Add click listeners as parameters for better architecture
//    interface ProfileClickListener {
//        fun onEditProfileClicked()
//        fun onAddStoryClicked()
//    }
//
//    // Fix 2: Add nullable click listener
//    private var clickListener: ProfileClickListener? = null
//
//    fun setProfileClickListener(listener: ProfileClickListener) {
//        clickListener = listener
//    }
//
//    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        // Fix 3: Use view binding or proper view references
//        val profileImage: CircleImageView = itemView.findViewById(R.id.profileImage)
//        val userName: TextView = itemView.findViewById(R.id.userName)
//        val userBio: TextView = itemView.findViewById(R.id.userBio)
//        val editProfileButton: MaterialButton = itemView.findViewById(R.id.editProfileButton)
//        val addStoryButton: MaterialButton = itemView.findViewById(R.id.addStoryButton)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
//        // Fix 4: Use proper layout inflation
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_profile, parent, false)
//        return ProfileViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = profileList.size
//
//    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
//        val user = profileList[position]
//
//        // Fix 5: Handle null values properly
//        holder.userName.text = user.username ?: "Anonymous User"
//        holder.userBio.text = user.about ?: "No bio provided"
//
//        // Fix 6: Use the click listener interface instead of direct intents
//        holder.editProfileButton.setOnClickListener {
//            clickListener?.onEditProfileClicked() ?: run {
//                context.startActivity(Intent(context, EditProfileActivity::class.java))
//            }
//        }
//
//        holder.addStoryButton.setOnClickListener {
//            clickListener?.onAddStoryClicked() ?: run {
//                context.startActivity(Intent(context, CreateActivity::class.java))
//            }
//        }
//
//        // Fix 7: Improved image loading with error handling
//        loadProfileImage(user.profile, holder.profileImage)
//    }
//
//    private fun loadProfileImage(encodedImage: String?, imageView: CircleImageView) {
//        if (encodedImage.isNullOrEmpty()) {
//            imageView.setImageResource(R.drawable.profile)
//            return
//        }
//
//        try {
//            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
//            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//            imageView.setImageBitmap(decodedBitmap)
//        } catch (e: Exception) {
//            // Fix 8: Handle all exceptions, not just IllegalArgumentException
//            imageView.setImageResource(R.drawable.profile)
//        }
//    }
//}