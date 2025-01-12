package com.example.talktrends.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talktrends.R
import com.example.talktrends.model.Post

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder class to bind the views
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postText)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Set the text content
        holder.postText.text = post.text

        // Set the image using Glide
        if (post.imageUri != null) {
            Glide.with(holder.postImage.context)
                .load(post.imageUri)               // Load the URI
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image during loading
                .error(R.drawable.ic_launcher_foreground)           // Error image if loading fails
                .into(holder.postImage)
        } else {
            // If no image URI, set a default placeholder
            holder.postImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
