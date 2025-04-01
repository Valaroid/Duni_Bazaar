package com.example.dunibazar.model.repository.cart

import android.content.SharedPreferences
import com.example.dunibazar.model.data.CheckOut
import com.example.dunibazar.model.data.SubmitOrder
import com.example.dunibazar.model.data.UserCartInfo
import com.example.dunibazar.model.net.ApiService
import com.example.dunibazar.utils.NO_PAYMENT
import com.google.gson.JsonObject


class CartRepositoryImp(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : CartRepository {


    override suspend fun addToCart(productId: String): Boolean {

        val json = JsonObject().apply {
            addProperty("productId", productId)
        }

        val result = apiService.addToCart(json)

        return result.success

    }

    override suspend fun removeFromCart(productId: String): Boolean {

        val json = JsonObject().apply {
            addProperty("productId", productId)
        }

        val result = apiService.removeFromCart(json)

        return result.success

    }

    override suspend fun getCartSize(): Int {

        val result = apiService.getUserCart()
        var counter = 0

        if (result.success) {
            result.productList.forEach {
                counter += (it.quantity ?: "0").toInt()

            }
        }

        return counter


    }

    override suspend fun getUserCartInto(): UserCartInfo {

        return apiService.getUserCart()

    }


    override suspend fun submitOrder(address: String, postalCode: String): SubmitOrder {
    val json = JsonObject().apply {
        addProperty("address", address)
        addProperty("postalCode", postalCode)
    }
        val result = apiService.submitOrder(json)
        setOrderId(result.orderId.toString())
        return result
    }

    override suspend fun getCheckOut(orderId: String): CheckOut {

        val json = JsonObject().apply {
           addProperty("orderId",orderId)
       }

        val result = apiService.checkOut(json)
        return result
    }

    override fun setOrderId(orderId: String) {
     sharedPreferences.edit().putString("orderId",orderId).apply()

    }

    override fun getOrderId(): String {
        return sharedPreferences.getString("orderId","0")!!
    }

    override fun setPurchaseStatus(status: Int) {
    sharedPreferences.edit().putInt("purchase_status",status).apply()
    }

    override fun getPurchaseStatus(): Int {
        return  sharedPreferences.getInt("purchase_status", NO_PAYMENT)
    }


}