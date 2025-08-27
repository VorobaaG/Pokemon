package com.example.pockemonapp.app.di
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import com.example.pockemonapp.data.PokemonRemoteMediator
import com.example.pockemonapp.data.local.PokemonDB
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


@OptIn(ExperimentalPagingApi::class)
val appModule = module{

    viewModelOf(::HomeBodyViewModel)

    single {
        val db = get<PokemonDB>()
        Pager(
            config = PagingConfig(
                pageSize = 40,
                initialLoadSize = 80,
                prefetchDistance = 40
            ),
            remoteMediator = PokemonRemoteMediator(service= get(),db=get()),
            pagingSourceFactory = {db.dao.pagingSource()}
        )
    }

}