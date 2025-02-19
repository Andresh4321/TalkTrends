package com.example.talktrends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.talktrends.R
import com.example.talktrends.model.CommentModel
import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.BitmapFactory
import android.util.Base64

class CommentAdapter(private var comments: List<CommentModel>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

        // ViewHolder class to bind the views
        class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userAvatar: CircleImageView = itemView.findViewById(R.id.ivUserAvatar)
            val userName: TextView = itemView.findViewById(R.id.tvUserName)
            val commentText: TextView = itemView.findViewById(R.id.tvCommentText)
            val timeAgo: TextView = itemView.findViewById(R.id.tvTimeAgo) // Optional, if you want to show time
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CommentViewHolder(view)
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            val comment = comments[position]

            // Set user name and comment text
            holder.userName.text = comment.username
            holder.commentText.text = comment.text

            // Load user avatar from Base64 string
            if (!comment.profileimage.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(comment.profileimage, Base64.DEFAULT)
                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    holder.userAvatar.setImageBitmap(decodedBitmap)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    holder.userAvatar.setImageResource(R.drawable.profile) // Placeholder image
                }
            } else {
                holder.userAvatar.setImageResource(R.drawable.profile) // Placeholder image
            }

            // Optional: Set time ago if you have a timestamp in your CommentModel
            // holder.timeAgo.text = getTimeAgo(comment.timestamp) // Implement this method if needed
        }

        // Method to update comments in the adapter
        fun updateComments(newComments: List<CommentModel>) {
            comments = newComments
            notifyDataSetChanged() // Notify the adapter to refresh the view
        }
    }