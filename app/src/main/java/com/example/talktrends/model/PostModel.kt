package com.example.talktrends.model

import android.os.Parcel
import android.os.Parcelable
import java.net.URL

data class PostModel(
    var postId:String="",
    var text:String="",
    var image: String? =null
){
}

