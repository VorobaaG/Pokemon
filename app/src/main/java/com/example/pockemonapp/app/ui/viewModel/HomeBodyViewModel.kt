package com.example.pockemonapp.app.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.mappers.toPokemon
import com.example.pockemonapp.data.mappers.toTypeFilter
import com.example.pockemonapp.data.mappers.toTypePokemon
import com.example.pockemonapp.data.remote.PokemonApi
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.TypeFilter
import com.example.pockemonapp.domain.model.TypeSort
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class HomeBodyViewModel(
    private val service:PokemonApi,
    private val db: PokemonDB,

) :ViewModel(){


    val currentSort = mutableStateOf(TypeSort.NONE)
    val currentType = mutableStateOf(TypeFilter.NONE)

    private val pager: Pager<Int, PokemonEntity> =  Pager(
        config = PagingConfig(
            pageSize = 40,
            initialLoadSize = 80,
            prefetchDistance = 40
        ),
        remoteMediator =
            if(currentType.value == TypeFilter.NONE) PokemonRemoteMediator(service= service,db=db)
                         else PokemonRemoteMediator(service= service,db=db) ,
        pagingSourceFactory = {
            when(currentSort.value){
                TypeSort.NONE -> db.pokemonDao.pagingSource()
                TypeSort.NAME -> db.pokemonDao.sortNyNamePagingSource()
                TypeSort.DAMAGE_INCREASE -> db.pokemonDao.sortByAttackIncreasePagingSource()
                TypeSort.DAMAGE_DECREASE -> db.pokemonDao.sortByAttackDecreasePagingSource()
                TypeSort.HP_INCREASE -> db.pokemonDao.sortByHpIncreasePagingSource()
                TypeSort.HP_DECREASE -> db.pokemonDao.sortByHpDecreasePagingSource()
                TypeSort.DEFENCE_INCREASE -> db.pokemonDao.sortByDefenceIncreasePagingSource()
                TypeSort.DEFENCE_DECREASE -> db.pokemonDao.sortByDefenceDecreasePagingSource()
            }

        })





    val pokemonPagingFlow = pager
        .flow
        .map {pokemonEntity->
            if(currentType.value==TypeFilter.NONE) {pokemonEntity.map { it.toPokemon() }}
            else{
                pokemonEntity
                    .filter {
                        val a =db.pokemonDao.getTypeById(it.id)
                        a?.forEach {name-> if( currentType.value.name.lowercase() == name) return@filter true }
                        return@filter false
                    }.map {
                        it.toPokemon()
                    }
            }
        }
        .cachedIn(viewModelScope)

}