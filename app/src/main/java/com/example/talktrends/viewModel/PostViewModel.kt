package com.example.talktrends.viewModel

import android.content.Context
import android.net.Uri
import com.example.talktrends.Repositary.PostRepository
import com.example.talktrends.model.PostModel

class PostViewModel(var repo:PostRepository) {
    fun addPost(PostModel: PostModel, callback: (Boolean, String) -> Unit){
        repo.addPost(PostModel,callback)
    }

    fun updatePost(PostId:String,data:MutableMap<String,Any>,callback: (Boolean, String) -> Unit){}

    fun deletePost(PostId:String,callback: (Boolean, String) -> Unit){
        repo.deletePost(PostId,callback)
    }

    fun updateLikes(postId: String, newLikes: Int, callback: (Boolean, String) -> Unit) {
        repo.updateLikes(postId, newLikes, callback)
    }

    fun getPostById(PostId: String,callback: (PostModel?,Boolean, String) -> Unit){}

    fun getPostsByUser (userId: String, callback: (List<PostModel>?, Boolean, String) -> Unit) {
        repo.getPostsByUser (userId, callback)
    }

    fun getAllPosts(callback: (List<PostModel>?, Boolean, String) -> Unit){
        repo.getAllPosts(callback)

    }


}