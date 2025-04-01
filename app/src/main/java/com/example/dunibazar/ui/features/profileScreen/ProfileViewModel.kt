package com.example.dunibazar.ui.features.profileScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dunibazar.model.repository.user.UserRepository


class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val address = mutableStateOf("")
    val postalCode = mutableStateOf("")
    val loginTime = mutableStateOf("")

    val showDialog = mutableStateOf(false)


    fun loadData(){
        val location = userRepository.getUserLocation()

        email.value=userRepository.getUserName()!!

        address.value=location.first
        postalCode.value=location.second

        loginTime.value=userRepository.getUserLoginTime()

    }


    fun saveLocation(address :String,postalCode : String){

        userRepository.saveUserLocation(address,postalCode)
    }


    fun signOut(){
        userRepository.singOut()
    }


}