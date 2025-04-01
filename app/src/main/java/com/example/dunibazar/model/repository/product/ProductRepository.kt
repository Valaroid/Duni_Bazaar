package com.example.dunibazar.model.repository.product

import com.example.dunibazar.model.data.Ads
import com.example.dunibazar.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetOk : Boolean) : List<Product>
    suspend fun getAllAds(isInternetOk : Boolean) : List<Ads>
    suspend fun getAllDataByCategory(category : String) : List<Product>

    suspend fun getProductById(productId : String):Product



}