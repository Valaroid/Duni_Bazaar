package com.example.dunibazar.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dunibazar.model.data.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(listProducts : List<Product>)


    @Query("Select * from product_table ")
    suspend fun getAllProducts(): List<Product>


    @Query("Select * from product_table where productId = :productId")
    suspend fun getProductById(productId: String) : Product

    @Query("Select * from product_table where category = :category")
    suspend fun getDataByCategory(category: String) :List<Product>

}