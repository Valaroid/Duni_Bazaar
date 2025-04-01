package com.example.dunibazar.model.repository.user

import android.content.SharedPreferences
import com.example.dunibazar.model.net.ApiService
import com.example.dunibazar.model.repository.TokenInMemory
import com.example.dunibazar.utils.SUCCESS_MESSAGE
import com.google.gson.JsonObject


class UserRepositoryImp(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun singUp(name: String, userName: String, password: String) : String {
        val json = JsonObject().apply {
            addProperty("name",name)
            addProperty("email",userName)
            addProperty("password",password)
        }
        val result=apiService.singUp(json)

        if (result.success){
            saveToken(result.token!!)
            saveUserName(userName)

            saveUserLoginTime()

            TokenInMemory.refreshToken(userName,result.token)
            return SUCCESS_MESSAGE
        }else{
            return result.message
        }


    }

    override suspend fun singIn(userName: String, password: String): String {
        val json = JsonObject().apply {
            addProperty("email",userName)
            addProperty("password",password)
        }
        val result=apiService.signIn(json)

        if (result.success){

            TokenInMemory.refreshToken(userName,result.token)
            saveToken(result.token!!)

            saveUserName(userName)

            saveUserLoginTime()

            return SUCCESS_MESSAGE

        }else{
            return result.message
        }
    }

    override fun singOut() {
        TokenInMemory.refreshToken(null,null)
        sharedPreferences.edit().clear().apply()
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString("token",token).apply()
    }

    override fun getToken(): String? {
       return sharedPreferences.getString("token",null)

    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(),getToken())
    }

    override fun saveUserName(userName: String) {
        sharedPreferences.edit().putString("userName",userName).apply()
    }


    override fun getUserName(): String? {
        return sharedPreferences.getString("userName",null)
    }

    override fun saveUserLocation(address: String, postalCode: String) {
        sharedPreferences.edit().putString("address",address).apply()
        sharedPreferences.edit().putString("postalCode",postalCode).apply()
    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sharedPreferences.getString("address","Click to Add")!!
        val postalCode = sharedPreferences.getString("postalCode","Click to Add")!!
        val location = Pair(address,postalCode)
        return location
    }

    override fun saveUserLoginTime() {
        val loginTime = System.currentTimeMillis()
        sharedPreferences.edit().putString("loginTime",loginTime.toString())
    }

    override fun getUserLoginTime(): String {
        return sharedPreferences.getString("loginTime","0")!!
    }


}