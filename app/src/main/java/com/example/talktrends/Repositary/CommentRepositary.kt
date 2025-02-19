package com.example.talktrends.Repositary

import com.example.talktrends.model.CommentModel
import com.example.talktrends.model.PostModel

interface CommentRepositary {

    fun addComment(comment: CommentModel, callback: (Boolean, String) -> Unit)

    fun getCommentByPost(postId: String, callback: (List<CommentModel>?, Boolean, String) -> Unit)



}