package com.example.pockemonapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.mappers.toPokemonEntity
import com.example.pockemonapp.data.remote.PokemonApi
import com.example.pockemonapp.domain.model.TypeFilter
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
class PokemonRemoteMediatorWithFilter(
    private val service: PokemonApi,
    private val db: PokemonDB,
    private val typeFilter: List<TypeFilter>,
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

           withContext(Dispatchers.IO) {
               val pokemonEntity: List<PokemonEntity>?

                   val response = typeFilter.map {
                       service.getPokemonByType(it.ordinal)
                   }
                  val res =  response
                      .map{it.pokemon.map { it.pokemon.name }.toSet()}
                      .reduceOrNull{acc,element -> acc.intersect(element)}
                      ?.toList()

                   val pokemon = coroutineScope {
                       res?.map {
                           async(SupervisorJob()) {
                               service.getPokemonByName(it!!)
                           }
                       }?.awaitAll()
                   }

                   pokemonEntity = pokemon?.map { it.toPokemonEntity() }

                   if (loadType == LoadType.REFRESH) {
                       offset = state.config.pageSize
                       db.pokemonDao.clearAll()
                   }
                   if(pokemonEntity!=null )db.pokemonDao.insertAll(pokemonEntity)

                   MediatorResult.Success(
                       endOfPaginationReached = true
                   )
               }

       }catch(e: IOException) {
           MediatorResult.Error(e)
       } catch(e: HttpException) {
           MediatorResult.Error(e)
       }

    }


}