package com.example.pockemonapp.domain.model

data class Pokemon(
    val id:Int=0,
    val name:String="Undefine",
    val urlImage:String="Undefine",
    val statsPokemon: StatsPokemon = StatsPokemon()
)

data class StatsPokemon(
    val hp:Int=0,
    val damage:Int=0,
    val defence:Int=0
)

