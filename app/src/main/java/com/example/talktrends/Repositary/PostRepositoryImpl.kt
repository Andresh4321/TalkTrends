package com.example.talktrends.Repositary

import com.example.talktrends.model.PostModel
import com.example.talktrends.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepositoryImpl :PostRepository{

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val ref: DatabaseReference =database.reference.child("Posts")


    override fun addPost(PostModel: PostModel, callback: (Boolean, String) -> Unit) {
        var id=ref.push().key.toString()
        PostModel.postId=id

        ref.child(id).setValue(PostModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Post added successfully")
            }else(
                    callback(false,"${it.exception?.message}")
                    )
        }
    }

    override fun updatePost(
        PostId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(PostId).setValue(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Post added successfully")
            }else(
                    callback(false,"${it.exception?.message}")
                    )
        }
    }

    override fun deletePost(ProductId: String, callback: (Boolean, String) -> Unit) {

    }

    override fun getPostById(ProductId: String, callback: (PostModel?, Boolean, String) -> Unit) {


    }

    override fun getPostsByUser (userId: String, callback: (List<PostModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val posts = mutableListOf<PostModel>()
                    for (eachData in snapshot.children) {
                        val model = eachData.getValue(PostModel::class.java)
                        if (model != null) {
                            posts.add(model)
                        }
                    }
                    callback(posts, true, "Posts fetched successfully")
                } else {
                    callback(emptyList(), true, "No posts found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Error fetching posts: ${error.message}")
            }
        })
    }

    override fun getAllPosts(callback: (List<PostModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var post= mutableListOf<PostModel>()
                    for(eachData in snapshot.children){
                        var model=eachData.getValue(PostModel::class.java)
                        if(model!=null){
                            post.add(model)
                        }
                    }
                    callback(post,true,"Post fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Error fetching posts: ${error.message}")
            }

        })
    }


    override fun updateLikes(postId: String, newLikes: Int, callback: (Boolean, String) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId)
        dbRef.updateChildren(mapOf("like" to newLikes))
            .addOnSuccessListener {
                callback(true, "Likes updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error updating likes")
            }
    }
}



