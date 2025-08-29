package com.example.pockemonapp.app.di
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.domain.model.TypeSort
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module{

    viewModelOf(::HomeBodyViewModel)



}