package com.example.pockemonapp.app.di

import androidx.room.Database
import androidx.room.Room
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonDao
import org.koin.dsl.module

val roomModule = module{

    single {
        Room.databaseBuilder(
            context = get(),
            klass = PokemonDB::class.java,
            name = "pockemon"
        )
            .build()
    }

    single{get<PokemonDB>().dao}



}