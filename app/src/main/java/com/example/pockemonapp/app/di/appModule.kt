package com.example.pockemonapp.app.di
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module{

    viewModel <HomeBodyViewModel>{ HomeBodyViewModel(get()) }

}