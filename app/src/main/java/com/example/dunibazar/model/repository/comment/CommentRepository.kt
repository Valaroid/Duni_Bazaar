package com.example.dunibazar.model.repository.comment


import com.example.dunibazar.model.data.Comment


interface CommentRepository {

    suspend fun getAllComments(productId : String) : List<Comment>
    suspend fun addNewComment(productId: String,text : String,onSuccess :(String)->Unit)


}