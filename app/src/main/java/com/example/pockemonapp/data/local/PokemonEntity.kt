package com.example.pockemonapp.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class PokemonEntity (
    @PrimaryKey
    val id:Int,
    val name:String,
    val urlImage: String,
    @Embedded
    val stat:StatsPokemonEntity
)

data class StatsPokemonEntity(
    val hp:Int =0,
    val attack:Int=0,
    val defence:Int=0,
    val specialAttack:Int=0,
    val specialDefence:Int=0,
    val speed:Int =0
)

@Entity
data class TypePokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val idOwnerPokemon:Int,
    val name:String
)

data class PokemonWithType(
    @Embedded val pokemon:PokemonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idOwnerPokemon"
    )
    val type: TypePokemonEntity
)


@Entity
data class DbState(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val timeLastUpdate:Long
)