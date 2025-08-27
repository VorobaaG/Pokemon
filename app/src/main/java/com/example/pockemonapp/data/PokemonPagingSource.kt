package com.example.pockemonapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil3.network.HttpException
import com.example.pockemonapp.data.remote.PokemonApi
import com.example.pockemonapp.data.remote.PokemonDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.IOException

class PokemonPagingSource(
    private val service:PokemonApi
) :PagingSource<Int,PokemonDto>(){

    private val PREFETCH_DISTANCE =80
    private var offset = PREFETCH_DISTANCE

    override fun getRefreshKey(state: PagingState<Int, PokemonDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonDto> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            var result:List<PokemonDto>

            withContext(Dispatchers.IO) {
                 result = coroutineScope {
                    val data = service.getListPokemon(limit = params.loadSize, offset = offset-PREFETCH_DISTANCE)
                    data.results?.map {
                        async(SupervisorJob()) { service.getPokemonByName(it.name ?: "") }
                    }?.awaitAll() ?: throw Exception("empty result")
                }
                offset+=params.loadSize
            }

            return LoadResult.Page(
                data = result,
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber+1
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error for
            // expected errors (such as a network failure).
            return LoadResult.Error(e)
        }
        catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}