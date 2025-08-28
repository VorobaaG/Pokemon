package com.example.pockemonapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

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

   @Query("SELECT * FROM PokemonEntity ORDER BY name")
   fun sortNyNamePagingSource():PagingSource<Int,PokemonEntity>

   @Delete
   fun delete(pokemon: PokemonEntity)

   @Transaction
   @Query("SELECT * FROM PokemonEntity WHERE id = :idOwnerPokemon")
   suspend fun getUsersAndLibraries(idOwnerPokemon:Int): PokemonAndStats

}