package com.example.dunibazar.model.repository.comment

import android.util.Log
import com.example.dunibazar.model.data.Comment
import com.example.dunibazar.model.net.ApiService
import com.google.gson.JsonObject


class CommentRepositoryImp(
    private val apiService: ApiService
)  : CommentRepository{
    override suspend fun getAllComments(productId: String): List<Comment> {

        val json =JsonObject().apply {
            addProperty("productId",productId)
        }

        val result=apiService.getAllComments(json)
        Log.v("test222",result.toString())

        if (result.success){
            return result.comments
        }

        return listOf()

    }

    override suspend fun addNewComment(productId: String, text: String, onSuccess: (String) -> Unit) {

        val json = JsonObject().apply {
            addProperty("productId",productId)
            addProperty("text",text)
        }

        val result = apiService.addNewComment(json)

        if (result.success){
            onSuccess.invoke(result.message)
        }else{
            onSuccess.invoke("Comment was not saved")
        }

    }


}