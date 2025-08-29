package com.example.pockemonapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [PokemonEntity::class,TypePokemonEntity::class],
    version = 1,
    exportSchema = false
)

abstract class PokemonDB: RoomDatabase(){
    abstract val pokemonDao: PokemonDao
}