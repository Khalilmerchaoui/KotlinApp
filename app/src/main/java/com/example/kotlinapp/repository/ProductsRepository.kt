package com.example.kotlinapp.repository

import com.example.kotlinapp.ApiService
import com.example.kotlinapp.model.Product
import com.example.kotlinapp.model.Products
import retrofit2.Call
import retrofit2.Response

class ProductsRepository(val netWorkApi: ApiService) {

    fun getProducts(onProductsDataListener: OnProductsDataListener) {
        netWorkApi.getProducts().enqueue(object : retrofit2.Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                onProductsDataListener.onSuccess((response.body() as Products))
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                onProductsDataListener.onFailure(t.message.toString())
            }
        })
    }

    interface OnProductsDataListener {
        fun onSuccess(data: Products)
        fun onFailure(errormsg : String)
    }
}