package com.example.pockemonapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

   @Insert
   fun insert(pokemon: PokemonEntity)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(pokemons:List<PokemonEntity>)

   @Query("SELECT * FROM PokemonEntity")
   fun pagingSource(): PagingSource<Int, PokemonEntity>

   @Query("DELETE FROM PokemonEntity")
   suspend fun clearAll()

   @Delete
   fun delete(pokemon: PokemonEntity)

}