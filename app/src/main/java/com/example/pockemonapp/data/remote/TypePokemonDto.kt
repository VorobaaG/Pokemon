package com.example.pockemonapp.data.remote

data class TypeDto(
    val pokemon :List<TypePokDto>
)

data class TypePokDto(
    val pokemon:ResultPokemonDto
)