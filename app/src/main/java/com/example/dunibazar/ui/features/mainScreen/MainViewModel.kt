package com.example.dunibazar.ui.features.mainScreen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.data.Ads
import com.example.dunibazar.model.data.CheckOut
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.model.repository.cart.CartRepository
import com.example.dunibazar.model.repository.product.ProductRepository
import com.example.dunibazar.utils.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val isInternetConnected: Boolean
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    var badgeNumber = mutableStateOf(0)


    val checkOut= mutableStateOf(CheckOut(null,null))
    val dialogPaymentResult = mutableStateOf(false)




    val showProgressBarState = mutableStateOf(false)

    init {
        refreshDataFromNet(isInternetConnected)
    }

    fun refreshDataFromNet(isInternetConnected: Boolean) {

        viewModelScope.launch(CoroutineExceptionHandler) {

            if (isInternetConnected) {
                showProgressBarState.value = true
            }


            val getDataProduct = async { productRepository.getAllProducts(isInternetConnected) }
            val getAds = async { productRepository.getAllAds(isInternetConnected) }


            updateData(getDataProduct.await(), getAds.await())

            delay(1200)


            showProgressBarState.value = false

        }

    }


    private fun updateData(productsList: List<Product>, adsList: List<Ads>) {

        dataProducts.value = productsList
        dataAds.value = adsList

    }

    fun getCartSize() {

        viewModelScope.launch(CoroutineExceptionHandler) {

            badgeNumber.value = cartRepository.getCartSize()

        }


    }

    fun getCheckOut(){

        viewModelScope.launch(CoroutineExceptionHandler) {
            val result=cartRepository.getCheckOut(cartRepository.getOrderId())

            if (result.success!!){
                checkOut.value=result
                dialogPaymentResult.value=true
            }



        }


    }


    fun getStatusPayment() : Int {
        return cartRepository.getPurchaseStatus()
    }


    fun setStatusPayment(status : Int){
        cartRepository.setPurchaseStatus(status)
    }



}