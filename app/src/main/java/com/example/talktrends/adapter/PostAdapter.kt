package com.example.talktrends.adapters


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

class PostAdapter(private val posts: List<PostModel>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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
        holder.itemView.findViewById<Button>(R.id.likeButton)?.apply {
            setBackgroundResource(R.color.light_Red) // Ensure it's consistent
            setTextColor(holder.itemView.context.resources.getColor(android.R.color.white, null))
        }
    }


    override fun getItemCount(): Int {
        return posts.size
    }
}
