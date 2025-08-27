package com.example.pockemonapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [PokemonEntity::class, StatsPokemonEntity::class],
    version = 1
)

abstract class PokemonDB: RoomDatabase(){
    abstract val dao: PokemonDao
}