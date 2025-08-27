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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val service: PokemonApi,
    private val db: PokemonDB
): RemoteMediator<Int, PokemonEntity>() {

    override suspend fun initialize(): InitializeAction {
        return super.initialize()

    }

    private  val PREFETCH_DISTANCE = 40
    private  var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
       return try {
           val loadKey = when(loadType){
               LoadType.REFRESH -> offset
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
                           service.getPokemonByName(it.name!!).toPokemonEntity()
                       }
                   }?.awaitAll()
               }


           db.withTransaction {
               if(loadType == LoadType.REFRESH){
                    db.dao.clearAll()
               }
               if(pokemon!=null) db.dao.insertAll(pokemon)
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