package com.example.talktrends.Repositary

import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel

interface PostRepository {
    fun updateLikes(postId: String, newLikes: Int, callback: (Boolean, String) -> Unit)

    fun addPost(PostModel: PostModel, callback: (Boolean, String) -> Unit)

    fun updatePost(PostId:String,data:MutableMap<String,Any>,callback: (Boolean, String) -> Unit)

    fun deletePost(PostId:String,callback: (Boolean, String) -> Unit)

    fun getPostById(PostId: String,callback: (PostModel?,Boolean, String) -> Unit)


    fun getAllPosts(callback: (List<PostModel>?, Boolean, String) -> Unit)
}