package com.example.pockemonapp.data.mappers

import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.data.local.StatsPokemonEntity
import com.example.pockemonapp.data.local.TypePokemonEntity
import com.example.pockemonapp.data.remote.PokemonDto
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.StatsPokemon
import com.example.pockemonapp.domain.model.TypeFilter
import com.example.pockemonapp.domain.model.TypeFilter.entries
import com.example.pockemonapp.domain.model.TypePokemon

fun PokemonDto.toPokemonEntity(): PokemonEntity{
    return PokemonEntity(
        id = id,
        name = name,
        urlImage = sprites.frontDefault?:"",
        stat = StatsPokemonEntity(
            hp = this.stats[0].baseStat,
            attack = this.stats[1].baseStat,
            defence = this.stats[2].baseStat,
            specialAttack = this.stats[3].baseStat,
            specialDefence = this.stats[4].baseStat,
            speed = this.stats[5].baseStat
        )
    )
}



fun PokemonEntity.toPokemon():Pokemon{
    return Pokemon(
        id = id,
        name = name,
        urlImage = urlImage,
        statsPokemon = StatsPokemon(
            hp = this.stat.hp,
            damage = this.stat.attack,
            defence = this.stat.defence
        ),
    )
}

fun TypePokemonEntity.toTypePokemon(): TypePokemon {
    return TypePokemon(entries.firstOrNull{it.name.equals(this.name,ignoreCase = true)}?: TypeFilter.NONE)
}

fun TypePokemonEntity.toTypeFilter(): TypeFilter {
    return entries.firstOrNull{it.name.equals(this.name,ignoreCase = true)}?: TypeFilter.NONE
}



fun String.ToTypePokemon(): TypePokemon {
    return TypePokemon(entries.firstOrNull{it.name.equals(this,ignoreCase = true)}?: TypeFilter.NONE)
}