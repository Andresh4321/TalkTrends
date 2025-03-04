package com.example.talktrends.viewModel

import androidx.lifecycle.ViewModel
import com.example.talktrends.Repositary.CommentRepositary
import com.example.talktrends.Repositary.UserRepository
import com.example.talktrends.model.CommentModel
import com.example.talktrends.model.PostModel

class CommentViewModel(private val repo: CommentRepositary) : ViewModel() {
    fun addComment(comment: CommentModel, callback: (Boolean, String) -> Unit) {
        repo.addComment(comment, callback)
    }

    fun getCommentByPost(postId: String, callback: (List<CommentModel>?, Boolean, String) -> Unit) {
        repo.getCommentByPost(postId, callback)
    }
}