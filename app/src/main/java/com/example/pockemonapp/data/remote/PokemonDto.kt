package com.example.pockemonapp.data.remote

import com.google.gson.annotations.SerializedName

data class PokemonDto(
   val id:Int,
   val name: String,
   val sprites:SpritesPokemonDto
)

data class SpritesPokemonDto(
    @SerializedName("front_default")
    val frontDefault: String?
)
