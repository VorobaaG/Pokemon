package com.example.pockemonapp.app.ui.screen

import com.example.pockemonapp.R
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import com.example.pockemonapp.data.local.PokemonEntity
import com.example.pockemonapp.domain.model.Pokemon
import com.example.pockemonapp.domain.model.TypeFilter
import com.example.pockemonapp.domain.model.TypeSort
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    vm:HomeBodyViewModel= koinViewModel(),
    homeModifier: Modifier
){
    val pagingPokemon = vm.pokemonPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()






        LaunchedEffect(key1 = pagingPokemon.loadState) {
            if (pagingPokemon.loadState.refresh is LoadState.Error) {
                Toast.makeText(
                    context,
                    "Error: " + (pagingPokemon.loadState.refresh as LoadState.Error).error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
            HomeBody(
                pokemons = pagingPokemon,
                gridState = lazyGridState,
                currentFilter = vm.currentType.value,
                onClickFilter = {
                    vm.currentType.value=it
                    pagingPokemon.refresh()
                    },
                modifier = homeModifier,
                currentSort = vm.currentSort.value,
                loadState = pagingPokemon.loadState.refresh,
                onClickSort = {
                    vm.currentSort.value = it
                    pagingPokemon.refresh()
                              },


                )


}

@Composable
fun HomeBody(
    pokemons: LazyPagingItems<Pokemon>,
    gridState: LazyGridState,
    onClickSort:(TypeSort)->Unit,
    currentSort:TypeSort =TypeSort.NONE,
    currentFilter:TypeFilter = TypeFilter.NONE,
    onClickFilter:(TypeFilter)->Unit,
    loadState: LoadState,
    modifier: Modifier
){


    Box(modifier = Modifier.then(modifier).fillMaxSize()) {

  Column {
      Box(modifier = Modifier.height(40.dp).padding(start =5.dp,end=5.dp).fillMaxWidth()){
          LazyRow(
              modifier = Modifier.fillMaxWidth(),
              ) {
              items(TypeSort.entries) {
                  Text(
                      modifier = Modifier.clickable { onClickSort(it)}
                          .padding(start = 8.dp)
                          .background(if(currentSort==it) MaterialTheme.colorScheme.primaryContainer
                          else MaterialTheme.colorScheme.background)
                          .clip(MaterialTheme.shapes.small)
                          .padding(start = 5.dp,end=5.dp,top=5.dp, bottom = 5.dp)
                          ,
                      text = "$it")
              }

          }
      }

      Box(modifier = Modifier.height(40.dp).padding(start =5.dp,end=5.dp).fillMaxWidth()){
          LazyRow(
              modifier = Modifier.fillMaxWidth(),
          ) {
              items(TypeFilter.entries) {
                  Text(
                      modifier = Modifier.clickable { onClickFilter(it)}
                          .padding(start = 8.dp)
                          .background(if(currentFilter==it) MaterialTheme.colorScheme.primaryContainer
                          else MaterialTheme.colorScheme.background)
                          .clip(MaterialTheme.shapes.small)
                          .padding(start = 5.dp,end=5.dp,top=5.dp, bottom = 5.dp)
                      ,
                      text = "$it")
              }

          }
      }
      if (loadState is LoadState.Loading) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              CircularProgressIndicator(                )
          }
      }else {
          LazyVerticalGrid(
              columns = GridCells.Adaptive(minSize = 120.dp),
              state = gridState,
              modifier = Modifier.padding(start = 8.dp, end = 8.dp)
          ) {
              items(
                  pokemons.itemCount,
                  key = { it }
              ) {
                  pokemons[it]?.let { PokemonItem(it) }

              }
              item {
                  if (pokemons.loadState.append is LoadState.Loading) {
                      CircularProgressIndicator()
                  }
              }
          }
      }
  }
  }
}

@Composable
fun PokemonItem(
pokemon:Pokemon
){
    Column(modifier = Modifier.padding(top= 8.dp)){
        Text(pokemon.name, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        SubcomposeAsyncImage(
            model = pokemon.urlImage,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(100.dp),
            loading = {CircularProgressIndicator()},
            error = { Image(
                painter = painterResource(R.drawable.baseline_broken_image_24),
                contentDescription = "ImageNotLoad",
                contentScale = ContentScale.Crop,

                ) }
        )
        Column {
            Text(text = "dmg:${pokemon.statsPokemon.damage}")
            Text(text = "hp:${pokemon.statsPokemon.hp}")
            Text(text = "def:${pokemon.statsPokemon.defence}")
        }
    }
}

@Preview(device = "spec:width=1080px,height=2340px,dpi=440", showSystemUi = true,
    showBackground = true
)
@Composable
fun previewPokemonItem(){
    val gridState = rememberLazyGridState()

    val pok = PagingData.from(List(20){Pokemon()})
    val pokemons = flowOf(pok).collectAsLazyPagingItems()
    HomeBody(
        gridState = gridState,
        onClickFilter = {},
        pokemons =pokemons,
        modifier = Modifier.padding(top=20.dp),
        loadState = LoadState.Loading,
        currentSort = TypeSort.NONE,
        onClickSort = {},
        currentFilter = TypeFilter.NONE
    )
}