package com.example.dunibazar.ui.features.productScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.data.Comment
import com.example.dunibazar.model.repository.cart.CartRepository
import com.example.dunibazar.model.repository.comment.CommentRepository
import com.example.dunibazar.model.repository.product.ProductRepository
import com.example.dunibazar.utils.CoroutineExceptionHandler
import com.example.dunibazar.utils.EMPTY_PRODUCT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
): ViewModel() {

    var thisProduct = mutableStateOf(EMPTY_PRODUCT)
    var comments= mutableStateOf(listOf<Comment>())
    var isAddingProduct = mutableStateOf(false)
    var badgeNumber = mutableStateOf(0)



    fun loadData(productId : String, isInternetConnected : Boolean){
        loadCashData(productId)

        if (isInternetConnected){
            loadComments(productId)
            getCartSize()
        }


    }



    private fun loadCashData(productId : String){

        viewModelScope.launch (CoroutineExceptionHandler){

            thisProduct.value=productRepository.getProductById(productId)
        }

    }

    private fun loadComments(productId: String){

        viewModelScope.launch (CoroutineExceptionHandler){

            comments.value=commentRepository.getAllComments(productId)

        }

    }

     fun addNewComment (productId: String, text : String, onSuccess :(String)->Unit){

         viewModelScope.launch (CoroutineExceptionHandler){

             commentRepository.addNewComment(productId,text,onSuccess)

             delay(100)

             comments.value=commentRepository.getAllComments(productId)

         }



    }



    fun addToCart(productId: String,addToCart:(String)->Unit){

        viewModelScope.launch (CoroutineExceptionHandler){
            isAddingProduct.value=true

           val result=cartRepository.addToCart(productId)
            if (result){
                addToCart.invoke("Product added to cart")
            }else{
                addToCart.invoke("Product does not add to cart")
            }

            delay(500)

            isAddingProduct.value=false

        }

    }


    private fun getCartSize(){

        viewModelScope.launch (CoroutineExceptionHandler){

            badgeNumber.value=cartRepository.getCartSize()

        }


    }



}