package com.example.dunibazar.model.repository.user

interface UserRepository {

    //online
    suspend fun singUp(name: String,userName: String,password:String) :String
    suspend fun singIn(userName: String ,password:String) :String
    fun singOut()


    //offline
    fun saveToken(token : String)
    fun getToken() : String?
    fun loadToken()

    fun saveUserName(userName : String)
    fun getUserName(): String?

    fun saveUserLocation(address : String,postalCode : String)
    fun getUserLocation() : Pair<String,String>

    fun saveUserLoginTime()
    fun getUserLoginTime() : String




}