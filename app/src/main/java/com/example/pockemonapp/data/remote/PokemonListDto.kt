package com.example.pockemonapp.data.remote

data class PokemonListDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ResultPokemonDto>? = null
)

data class ResultPokemonDto(
    val name: String?,
    val url: String?
)