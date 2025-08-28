package com.example.pockemonapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.local.StatsPokemonEntity
import com.example.pockemonapp.data.mappers.toPokemonEntity
import com.example.pockemonapp.data.remote.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val service: PokemonApi,
    private val db: PokemonDB
): RemoteMediator<Int, PokemonEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        //return if (System.currentTimeMillis() - db.lastUpdated() <= cacheTimeout)
       // {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
         return    InitializeAction.LAUNCH_INITIAL_REFRESH
       // } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
       //     InitializeAction.LAUNCH_INITIAL_REFRESH
       // }
    }

    private  var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
       return try {
           val loadKey = when(loadType){
               LoadType.REFRESH -> 0
               LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
               LoadType.APPEND -> {
                   val lastItem = state.lastItemOrNull()
                   if (lastItem == null) {
                       return MediatorResult.Success(
                           endOfPaginationReached = true
                       )
                   }
                   offset
               }
           }

           withContext(Dispatchers.IO) {
               val response = service.getListPokemon(limit = state.config.pageSize, loadKey)
               offset += state.config.pageSize
               val pokemon = coroutineScope {
                   response.results?.map {
                       async(SupervisorJob()) {
                           service.getPokemonByName(it.name!!)
                       }
                   }?.awaitAll()
               }

               val pokemonEntity = pokemon?.map { it.toPokemonEntity() }
               val stats = pokemonEntity?.mapIndexed { index, pokemonEn ->   StatsPokemonEntity(
                   idOwnerPokemon = pokemonEn.id,
                   hp= pokemon[index].stats[0].baseStat,
                   attack = pokemon[index].stats[1].baseStat,
                   defence = pokemon[index].stats[1].baseStat,

               ) }

               db.withTransaction {
                   if(loadType == LoadType.REFRESH){
                        offset = state.config.pageSize
                        db.pokemonDao.clearAll()
                        db.statsPokemonDao.clearAll()
                   }
                   if(pokemonEntity!=null) db.pokemonDao.insertAll(pokemonEntity)
                   if(stats !=null) db.statsPokemonDao.insertAll(stats)
               }

           MediatorResult.Success(
               endOfPaginationReached = (response.results==null)
           )
       }
       }catch(e: IOException) {
           MediatorResult.Error(e)
       } catch(e: HttpException) {
           MediatorResult.Error(e)
       }

    }


}