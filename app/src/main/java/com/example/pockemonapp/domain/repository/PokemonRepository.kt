package com.example.pockemonapp.domain.repository

import com.example.pockemonapp.domain.model.Pokemon

interface PokemonRepository {

    fun getPokemon():Pokemon
    fun getPokemons():List<Pokemon>
}