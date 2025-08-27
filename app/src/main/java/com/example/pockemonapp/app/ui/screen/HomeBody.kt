package com.example.pockemonapp.app.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import com.example.pockemonapp.data.remote.PokemonDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    vm:HomeBodyViewModel= koinViewModel()
){
    val pagingPokemon = vm.pager.collectAsLazyPagingItems()
    HomeBody(pokemons = pagingPokemon)
}

@Composable
fun HomeBody(
    pokemons: LazyPagingItems<PokemonDto>
){
   LazyVerticalGrid(
       columns = GridCells.Adaptive(minSize = 120.dp)
   ) {
       items(pokemons.itemCount,key ={it}) {
           Text("$it")
           AsyncImage(
               model =pokemons[it]?.sprites?.frontDefault?:"",
               contentDescription = pokemons[it]?.name,
              contentScale = ContentScale.Crop
           )
       }
   }
}