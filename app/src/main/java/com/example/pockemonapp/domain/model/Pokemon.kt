package com.example.pockemonapp.domain.model

import com.example.pockemonapp.data.local.TypePokemonEntity
import com.example.pockemonapp.domain.model.TypeFilter.entries

data class Pokemon(
    val id:Int=0,
    val name:String="Undefine",
    val urlImage:String="Undefine",
    val statsPokemon: StatsPokemon = StatsPokemon(),
    val typePokemon:List<TypePokemon> = emptyList()
)

data class StatsPokemon(
    val hp:Int=0,
    val damage:Int=0,
    val defence:Int=0
)

data class TypePokemon(
    val name:TypeFilter
)

