package com.example.dunibazar.ui.features.cartScreen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.model.repository.cart.CartRepository
import com.example.dunibazar.model.repository.user.UserRepository
import com.example.dunibazar.utils.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CartViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val totalPrice = mutableStateOf(0)
    val productList = mutableStateOf(listOf<Product>())

    val isChangingNumber = mutableStateOf(Pair("",false))


    fun loadData(){

        viewModelScope.launch(CoroutineExceptionHandler) {

            val dataCart = cartRepository.getUserCartInto()

            totalPrice.value=dataCart.totalPrice
            productList.value=dataCart.productList

        }

    }
    fun addToCart(productId: String){

        viewModelScope.launch(CoroutineExceptionHandler) {

            isChangingNumber.value=isChangingNumber.value.copy(first = productId, second = true)

            val isSuccess=cartRepository.addToCart(productId)
            if(isSuccess){
                loadData()
            }

            delay(100)

            isChangingNumber.value=isChangingNumber.value.copy(first = productId, second = false)

        }
    }
    fun removeFromCart(productId: String){
        viewModelScope.launch(CoroutineExceptionHandler) {

            isChangingNumber.value=isChangingNumber.value.copy(first = productId, second = true)

            val isSuccess=cartRepository.removeFromCart(productId)

            if(isSuccess){
                loadData()
            }

            delay(100)

            isChangingNumber.value=isChangingNumber.value.copy(first = productId, second = false)


        }
    }



    fun getUserLocation() : Pair<String,String>{
        return userRepository.getUserLocation()
    }

    fun saveLocation(address : String,postalCode: String){
        userRepository.saveUserLocation(address,postalCode)
    }


    fun submitOrder(address: String,postalCode: String , isSuccess:(Boolean,String)->Unit){

        viewModelScope.launch(CoroutineExceptionHandler) {


           val result= cartRepository.submitOrder(address, postalCode)

            isSuccess.invoke(result.success,result.paymentLink)


        }


    }

    fun saveStatusPayment(status : Int){
        cartRepository.setPurchaseStatus(status)
    }




}