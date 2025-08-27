package com.example.pockemonapp.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pockemonapp.data.PokemonPagingSource
import com.example.pockemonapp.data.remote.PokemonApi

class HomeBodyViewModel(
private val service:PokemonApi
) :ViewModel(){

    val pager = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(
            pageSize = 40,
            prefetchDistance = 80,
            initialLoadSize = 120
        )
    ) {
        PokemonPagingSource(service=service)
    }.flow
        .cachedIn(viewModelScope)

}