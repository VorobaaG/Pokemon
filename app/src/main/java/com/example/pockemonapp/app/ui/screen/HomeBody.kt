package com.example.pockemonapp.app.ui.screen

import com.example.pockemonapp.R
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import com.example.pockemonapp.data.local.PokemonEntity
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    vm:HomeBodyViewModel= koinViewModel()
){
    val pagingPokemon = vm.pokemonPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()
    LaunchedEffect(key1 = pagingPokemon.loadState) {
        if(pagingPokemon.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (pagingPokemon.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    if(pagingPokemon.loadState.refresh is LoadState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(

            )
        }
    }else {
        HomeBody(pokemons = pagingPokemon, gridState = lazyGridState)
    }
}

@Composable
fun HomeBody(
    pokemons: LazyPagingItems<PokemonEntity>,
    gridState: LazyGridState
){


   LazyVerticalGrid(
       columns = GridCells.Adaptive(minSize = 120.dp),
       state = gridState
   ) {
       items(
           pokemons.itemCount,
           key = {it}
           ) {
                Text("${it}")
            SubcomposeAsyncImage(
                   model = pokemons[it]?.urlImage?: "",
                   contentDescription = pokemons[it]?.name,
                   contentScale = ContentScale.Crop,
                   loading = {CircularProgressIndicator()},
                   error = { Image(
                        painter = painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = "ImageNotLoad",
                       modifier = Modifier.height(110.dp).width(110.dp)

                        ) }


               )
       }
       item {
           if(pokemons.loadState.append is LoadState.Loading) {
               CircularProgressIndicator()
           }
       }
   }
}