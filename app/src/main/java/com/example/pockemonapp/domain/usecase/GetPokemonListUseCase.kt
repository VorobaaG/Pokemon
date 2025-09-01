package com.example.pockemonapp.domain.usecase

import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.repository.PokemonRepository

class GetPokemonUseCase(private val repository:PokemonRepository) {

    fun execute():Pokemon{
        return repository.getPokemon()
    }

}