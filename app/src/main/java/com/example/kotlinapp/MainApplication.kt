package com.example.kotlinapp

import android.app.Application
import com.example.kotlinapp.repository.ProductsRepository
import com.example.kotlinapp.viewModel.ProductsViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class MainApplication : Application() {

    val mainModule = module {

        single { ProductsRepository(get()) }

        single { ApiService() }

        viewModel { ProductsViewModel(get()) }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this,
            listOf(mainModule),
            loadPropertiesFromFile = true)
    }
}