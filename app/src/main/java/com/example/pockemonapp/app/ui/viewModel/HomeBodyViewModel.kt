package com.example.pockemonapp.app.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.mappers.toPokemon
import com.example.pockemonapp.data.remote.PokemonApi
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.TypeFilter
import com.example.pockemonapp.domain.model.TypeSort
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class HomeBodyViewModel(
    private val service:PokemonApi,
    private val db: PokemonDB,

) :ViewModel() {


    val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    val resultSearchPokemon: StateFlow<List<String>> = searchQuery
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest {
            flowOf(
                db.pokemonDao.findByNameStartingWith(it) ?: listOf()
            )
        }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.update { query }
        }
    }


    private val _currentSort = MutableStateFlow(TypeSort.NONE)
    val currentSort = _currentSort.asStateFlow()
    private val _currentFilter = MutableStateFlow(listOf(TypeFilter.NONE))
    val currentFilter = _currentFilter.asStateFlow()

    val pokemonPagingFlow = combine(
        _currentSort, _currentFilter
    ) { sort, type ->
        sort to type
    }
        .flatMapLatest { (sort, type) ->
            Pager(
                config = PagingConfig(
                    pageSize = 40,
                    initialLoadSize = 80,
                    prefetchDistance = 40
                ),
                remoteMediator =
                    PokemonRemoteMediator(service = service, db = db, type = type),
                pagingSourceFactory = {
                    when (sort) {
                        TypeSort.NONE -> db.pokemonDao.pagingSource()
                        TypeSort.NAME -> db.pokemonDao.sortNyNamePagingSource()
                        TypeSort.DAMAGE_INCREASE -> db.pokemonDao.sortByAttackIncreasePagingSource()
                        TypeSort.DAMAGE_DECREASE -> db.pokemonDao.sortByAttackDecreasePagingSource()
                        TypeSort.HP_INCREASE -> db.pokemonDao.sortByHpIncreasePagingSource()
                        TypeSort.HP_DECREASE -> db.pokemonDao.sortByHpDecreasePagingSource()
                        TypeSort.DEFENCE_INCREASE -> db.pokemonDao.sortByDefenceIncreasePagingSource()
                        TypeSort.DEFENCE_DECREASE -> db.pokemonDao.sortByDefenceDecreasePagingSource()
                    }
                }).flow
                .map {pokemonEntity->
                    pokemonEntity.map { it.toPokemon() }
                }
                .cachedIn(viewModelScope)
        }

    fun setTypeFilter(filter:TypeFilter){
            _currentFilter.update { changeType(filter) }
    }
    fun getTypeFilter():List<TypeFilter> = currentFilter.value


    fun setTypeSort(sort:TypeSort){
        _currentSort.update { sort }
    }

    fun getTypeSort():TypeSort = currentSort.value

    private fun changeType(type:TypeFilter):List<TypeFilter>{

        val filter = currentFilter.value.toMutableList()

        if(filter.contains(TypeFilter.NONE) && type !=TypeFilter.NONE){
            filter.remove(TypeFilter.NONE)
        }
        else if (type ==TypeFilter.NONE) return listOf(TypeFilter.NONE)

        if(filter.contains(type)) {
            filter.remove(type)
            return filter.toList()
        }else {
            filter.add(type)
            return filter.toList()
        }

    }

}