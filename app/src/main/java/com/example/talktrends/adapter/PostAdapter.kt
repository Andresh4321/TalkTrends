package com.example.talktrends.adapters


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.model.PostModel
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import com.example.talktrends.Repositary.UserRepositoryImpl
import com.example.talktrends.UI.Activity.CommentActivity
import com.example.talktrends.UI.Activity.ProfileActivity
import kotlin.contracts.contract

class PostAdapter(private val posts: List<PostModel>, private val onLikeClicked: (PostModel) -> Unit): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder class to bind the views
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileimage: ImageView=itemView.findViewById(R.id.ProfileImageView)
        val name:TextView=itemView.findViewById(R.id.usernameTextView)
        val postText: TextView = itemView.findViewById(R.id.postText)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val likeCount: TextView = itemView.findViewById(R.id.Like)
        val likeButton: Button = itemView.findViewById(R.id.likeButton)
        val comment:Button=itemView.findViewById(R.id.commentButton)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        if(!post.profileimage.isNullOrEmpty()){
            try {
                val decodedBytes = Base64.decode(post.profileimage, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.profileimage.setImageBitmap(decodedBitmap)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // Show an error placeholder if decoding fails
                holder.profileimage.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } else {
            // Show a placeholder if the image string is empty or null
            holder.profileimage.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.name.text=post.username

        // Set the text content
        holder.postText.text = post.text

        holder.profileimage.setOnClickListener {
            val context = holder.itemView.context
            val userRepository = UserRepositoryImpl() // Create an instance of your UserRepository

            userRepository.getUser(post.userId) { user, success, message ->
                if (success && user != null) {
                    // Start ProfileActivity with user details
                    val intent = Intent(context, ProfileActivity::class.java).apply {
                        putExtra("USER_ID", user.userId) // Assuming userId is a property in UserModel
                    }
                    context.startActivity(intent)
                } else {
                    // Handle error (e.g., show a Toast)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.comment.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CommentActivity::class.java).apply {
                putExtra("USER_ID", post.userId)
                putExtra("POST_ID", post.postId)
            }
            context.startActivity(intent)
        }

        // Decode the Base64 image string
        if (!post.image.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(post.image, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.postImage.setImageBitmap(decodedBitmap)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // Show an error placeholder if decoding fails
                holder.postImage.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } else {
            // Show a placeholder if the image string is empty or null
            holder.postImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
        // Set the like count
        holder.likeCount.text = "Likes: ${post.like}"

        // Handle the like button click
        holder.likeButton.setOnClickListener {
            // Increment the like count locally
            post.like += 1
            // Update the UI
            holder.likeCount.text = "Likes: ${post.like}"

            // Trigger the callback to update the database
            onLikeClicked(post)
        }


    }
    }



