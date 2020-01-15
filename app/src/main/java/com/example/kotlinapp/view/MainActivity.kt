package com.example.kotlinapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinapp.R
import com.example.kotlinapp.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = ApiService()

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.getProducts(1).await();
            txtResponse.text = response.products.toString()
        }
    }
}
