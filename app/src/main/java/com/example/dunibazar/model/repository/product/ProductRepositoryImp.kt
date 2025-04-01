package com.example.dunibazar.model.repository.product

import com.example.dunibazar.model.data.Ads
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.model.db.ProductDao
import com.example.dunibazar.model.net.ApiService

class ProductRepositoryImp(
    private val apiService: ApiService,
    private val productDao: ProductDao
)  : ProductRepository{


    override suspend fun getAllProducts(isInternetOk: Boolean): List<Product> {

        if (isInternetOk){

            val dataProductsFromServer = apiService.getAllProducts()
            if (dataProductsFromServer.success){
                productDao.insertOrUpdate(dataProductsFromServer.products)
                return dataProductsFromServer.products
            }


        }else{

            return productDao.getAllProducts()

        }

        return listOf()
    }



    override suspend fun getAllAds(isInternetOk: Boolean): List<Ads> {

        if (isInternetOk){

            val dataAdsFromServer = apiService.getAllAds()
            if (dataAdsFromServer.success){
                return dataAdsFromServer.ads
            }

        }


        return listOf()
    }

    override suspend fun getAllDataByCategory(category: String): List<Product> {
        return productDao.getDataByCategory(category)
    }

    override suspend fun getProductById(productId: String): Product {

        return productDao.getProductById(productId)
    }
}