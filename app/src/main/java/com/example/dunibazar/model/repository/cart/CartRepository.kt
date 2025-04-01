package com.example.dunibazar.model.repository.cart

import com.example.dunibazar.model.data.CheckOut
import com.example.dunibazar.model.data.SubmitOrder
import com.example.dunibazar.model.data.UserCartInfo


interface CartRepository {

   suspend fun addToCart(productId : String) : Boolean
   suspend fun removeFromCart(productId : String) : Boolean
   suspend fun getCartSize():Int
   suspend fun getUserCartInto() : UserCartInfo


   suspend fun submitOrder(address : String,postalCode:String): SubmitOrder
   suspend fun getCheckOut(orderId : String) : CheckOut

   fun setOrderId(orderId: String)
   fun getOrderId(): String

   fun setPurchaseStatus(status: Int)
   fun getPurchaseStatus(): Int



}