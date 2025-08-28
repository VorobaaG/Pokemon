package com.example.pockemonapp.data.mappers

import com.example.pockemonapp.data.local.PokemonAndStats
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.local.StatsPokemonEntity
import com.example.pockemonapp.data.remote.PokemonDto
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.StatsPokemon


fun PokemonDto.toPokemonEntity(): PokemonEntity{
    return PokemonEntity(
        id = id,
        name = name,
        urlImage = sprites.frontDefault?:"",

    )
}

fun PokemonAndStats.toPokemon():Pokemon{
    return Pokemon(
        id =this.pokemon.id,
        name = this.pokemon.name,
        urlImage = this.pokemon.urlImage,
        statsPokemon = StatsPokemon(
            hp = this.stats.hp,
            damage = this.stats.attack,
            defence = this.stats.defence
        )
    )
}


fun PokemonEntity.toPokemon():Pokemon{
    return Pokemon(
        id = id,
        name = name,
        urlImage = urlImage,
    )
}
