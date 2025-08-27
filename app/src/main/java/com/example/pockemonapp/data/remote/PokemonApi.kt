package com.example.pockemonapp.data.remote

import retrofit2.http.GET

interface PockemonApi{

    @GET("pokemon?offset=20&limit=20")
    suspend fun getListPockemons(): PockemonDto

}