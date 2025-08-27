package com.example.pockemonapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity (
    @PrimaryKey
    val id:Int,
    val name:String,
    val urlImage: String,
    val idStatsPokemon:Int //id для связи сStatsPokemonEntity
)

@Entity
data class StatsPokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val hp:Int =0,
    val attack:Int=0,
    val defence:Int=0,
    val specialAttack:Int=0,
    val specialDefence:Int=0,
    val speed:Int =0

)