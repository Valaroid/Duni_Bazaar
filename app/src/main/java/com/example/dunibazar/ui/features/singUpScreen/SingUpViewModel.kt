package com.example.dunibazar.ui.features.singUpScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.repository.user.UserRepository
import com.example.dunibazar.utils.CoroutineExceptionHandler

import kotlinx.coroutines.launch


class SingUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    val fullName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val passWord = MutableLiveData<String>("")
    val confirmPassword = MutableLiveData<String>("")


    fun singUpUser(loginEvent : (String)->Unit){

        viewModelScope.launch(CoroutineExceptionHandler){
            val result = userRepository.singUp(name = fullName.value!!, userName = email.value!!, password = passWord.value!!)
            loginEvent(result)
        }


    }



}