package com.example.talktrends.Repositary

import com.example.talktrends.model.CommentModel
import com.example.talktrends.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentRepositaryImpl: CommentRepositary {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("Comments")

    override fun addComment(comment: CommentModel, callback: (Boolean, String) -> Unit) {
        val commentId = ref.push().key // Generate a unique key for the comment
        if (commentId != null) {
            ref.child(commentId).setValue(comment).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Comment added successfully")
                } else {
                    callback(false, "Failed to add comment")
                }
            }
        } else {
            callback(false, "Failed to generate comment ID")
        }


    }
    // Get comments by post ID
    override fun getCommentByPost(postId: String, callback: (List<CommentModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("postId").equalTo(postId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val comments = mutableListOf<CommentModel>()
                    for (eachData in snapshot.children) {
                        val model = eachData.getValue(CommentModel::class.java)
                        if (model != null) {
                            comments.add(model)
                        }
                    }
                    callback(comments, true, "Comments fetched successfully")
                } else {
                    callback(emptyList(), true, "No comments found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Error fetching comments: ${error.message}")
            }
        })
    }
}