package com.example.dunibazar.ui.features.singInScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.repository.user.UserRepository
import com.example.dunibazar.utils.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class SingInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val email = MutableLiveData<String>("")
    val passWord = MutableLiveData<String>("")


    fun singInUser(loginEvent : (String)->Unit){

        viewModelScope.launch(CoroutineExceptionHandler) {
            val result = userRepository.singIn(userName = email.value!!, password = passWord.value!! )
            loginEvent(result)
        }


    }



}