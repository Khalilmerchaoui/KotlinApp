package com.example.kotlinapp

import com.example.kotlinapp.model.Product
import com.example.kotlinapp.model.Products
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// URL : https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6

const val  ACCESS_TOKEN = "c32313df0d0ef512ca64d5b336a0d7c6"

interface ApiService {

    @GET("products.json")
    fun getProducts(
        @Query("page") page : Int  = 1
    ) : Call<Products>

    companion object {
        operator fun invoke() : ApiService {
            val requestInterceptor = Interceptor {chain ->
                val url  = chain
                    .request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_token", ACCESS_TOKEN)
                    .build()

                val request = chain
                    .request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://shopicruit.myshopify.com/admin/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}