package com.example.pockemonapp.data.mappers

import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.remote.PokemonDto


fun PokemonDto.toPokemonEntity(): PokemonEntity{
    return PokemonEntity(
        id = id,
        name = name,
        urlImage = sprites.frontDefault?:"",
        idStatsPokemon = 0

    )
}

