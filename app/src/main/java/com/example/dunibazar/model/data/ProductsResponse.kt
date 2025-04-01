package com.example.dunibazar.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProductsResponse(
    val products: List<Product>,
    val success: Boolean
)


    @Entity(tableName = "Product_table")
    data class Product(

        val category: String,
        val detailText: String,
        val imgUrl: String,
        val material: String,
        val name: String,
        val price: String,

        @PrimaryKey
        val productId: String,

        val soldItem: String,
        val tags: String ,
        val quantity : String?
    )
