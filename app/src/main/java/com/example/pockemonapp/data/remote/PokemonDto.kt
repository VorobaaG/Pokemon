package com.example.pockemonapp.data.remote

import com.google.gson.annotations.SerializedName

data class PokemonDto(
   val id:Int,
   val name: String,
   val sprites:SpritesPokemonDto,
   val stats:List<StatsPokemonDto>,
   val types:List<TypesPokemonDto>
)

data class TypesPokemonDto(
    val type:TypePokemonDto
)

data class TypePokemonDto(
    val name:String="none"
)


data class SpritesPokemonDto(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class StatsPokemonDto(
    @SerializedName("base_stat")
   val baseStat:Int,
   val effort:Int,
   val stat: StatPokemonDto
)

data class StatPokemonDto(
    val name: String
)
