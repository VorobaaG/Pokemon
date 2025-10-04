package com.example.pockemonapp.app.di

import android.content.Context
import androidx.room.Room
import com.example.pockemonapp.data.local.PokemonDB
import com.example.pockemonapp.data.local.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun getDB(@ApplicationContext context: Context): PokemonDB {
    return Room.databaseBuilder(
            context = context,
            klass = PokemonDB::class.java,
            name = "pockemon"
        )
            .build()
    }

    @Provides
    fun getDao(database : PokemonDB) : PokemonDao{
        return database.pokemonDao
    }


}


