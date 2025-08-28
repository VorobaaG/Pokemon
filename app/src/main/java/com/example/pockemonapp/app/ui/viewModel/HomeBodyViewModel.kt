package com.example.pockemonapp.app.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.mappers.toPokemon
import com.example.pockemonapp.data.remote.PokemonApi
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.TypeSort
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class HomeBodyViewModel(
    private val service:PokemonApi,
    private val db: PokemonDB,

) :ViewModel(){


    val currentSort = mutableStateOf(TypeSort.NONE)
    private val pager: Pager<Int, PokemonEntity> =  Pager(
        config = PagingConfig(
            pageSize = 40,
            initialLoadSize = 80,
            prefetchDistance = 40
        ),
        remoteMediator = PokemonRemoteMediator(service= service,db=db),
        pagingSourceFactory = {
            when(currentSort.value){
                TypeSort.NONE -> db.pokemonDao.pagingSource()
                TypeSort.NAME -> db.pokemonDao.sortNyNamePagingSource()
                TypeSort.DAMAGE -> TODO()
                TypeSort.HP -> TODO()
            }

        })



    val pokemonPagingFlow = pager
        .flow
        .map {pokemonEntity-> pokemonEntity.map{
            db.pokemonDao.getUsersAndLibraries(it.id).toPokemon() }
        }
        .cachedIn(viewModelScope)

}