package com.example.talktrends.model


data class PostModel(
    var userId:String="",
    var username:String="",
    var profileimage:String?=null,
    var postId:String="",
    var text:String="",
    var image: String? =null,
    var genre:String?="",
    var like:Int=0,
    var timestamp: Long = System.currentTimeMillis()
){
}

