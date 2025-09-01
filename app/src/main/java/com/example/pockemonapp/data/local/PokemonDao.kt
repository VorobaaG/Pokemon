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

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(pokemons:List<PokemonEntity>)

   @Query("SELECT * FROM PokemonEntity ORDER BY id")
   fun pagingSource(): PagingSource<Int, PokemonEntity>

   @Query("DELETE FROM PokemonEntity")
   suspend fun clearAll()

   @Query("SELECT * FROM PokemonEntity ORDER BY name")
   fun sortNyNamePagingSource():PagingSource<Int,PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY attack")
   fun sortByAttackIncreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY hp")
   fun sortByHpIncreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY defence")
   fun sortByDefenceIncreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY attack DESC")
   fun sortByAttackDecreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY hp DESC")
   fun sortByHpDecreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT * FROM PokemonEntity ORDER BY defence DESC")
   fun sortByDefenceDecreasePagingSource(): PagingSource<Int, PokemonEntity>

   @Query("SELECT TypePokemonEntity.name FROM TypePokemonEntity WHERE TypePokemonEntity.idOwnerPokemon = :id")
   suspend fun getTypeById(id:Int):List<String>?

   @Delete
   fun delete(pokemon: PokemonEntity)

   @Query("SELECT name FROM PokemonEntity WHERE name LIKE :search || '%' ")
   suspend fun findByNameStartingWith(search:String) : List<String>?


}