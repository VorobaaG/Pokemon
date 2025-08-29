package com.example.pockemonapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi{

    @GET("pokemon")
    suspend fun getListPokemon(
        @Query("limit") limit:Int,
        @Query("offset")offset:Int): PokemonListDto

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name:String):PokemonDto

    @GET("type/{id}")
    suspend fun getPokemonByType(@Path("id") id:Int):List<ResultPokemonDto>


}