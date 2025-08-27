package com.example.pockemonapp.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonDao
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.remote.PokemonApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalPagingApi::class)
class HomeBodyViewModel(
    private val service:PokemonApi,
    private val db: PokemonDB,
    private val pager: Pager<Int, PokemonEntity>
) :ViewModel(){


    val pokemonPagingFlow = pager
        .flow
        .map { it }
        .cachedIn(viewModelScope)

}