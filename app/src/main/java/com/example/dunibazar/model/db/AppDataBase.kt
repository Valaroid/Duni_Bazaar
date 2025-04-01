package com.example.dunibazar.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dunibazar.model.data.Product

@Database(entities = [Product::class] , version = 1 , exportSchema = false)
abstract class AppDataBase  : RoomDatabase(){

    abstract val productDao : ProductDao

}