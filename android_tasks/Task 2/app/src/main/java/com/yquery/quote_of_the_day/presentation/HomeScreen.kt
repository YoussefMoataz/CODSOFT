package com.yquery.quote_of_the_day.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yquery.quote_of_the_day.R
import com.yquery.quote_of_the_day.presentation.components.FavouriteListItem
import com.yquery.quote_of_the_day.presentation.components.QuoteCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {

    val quotesViewModel = hiltViewModel<QuotesViewModel>()
    val quote by quotesViewModel.getQuote().collectAsState()
    val isFavourite = quotesViewModel.isFavourite(quote).collectAsState().value
    val quotesFavourites = quotesViewModel.getFavourites().collectAsState()

    val refreshing by quotesViewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { quotesViewModel.refreshQuote() })

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(id = R.string.app_name))
            }, actions = {
                IconButton(onClick = { quotesViewModel.refreshQuote() }) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Refresh Quote")
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                quote?.let { quotesViewModel.changeFavouriteStatus(it) }
            }) {
                Icon(
                    imageVector = if (isFavourite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = "Favourite"
                )
            }
        }
    ) { paddings ->

        Box(
            modifier = Modifier
                .padding(paddings)
                .pullRefresh(pullRefreshState)
        ) {

            Column {

                QuoteCard(quote = quote)

                Divider()

                LazyColumn {
                    items(items = quotesFavourites.value ?: emptyList()) {
                        FavouriteListItem(quote = it,
                                          favouriteItemClicked = {
                                              quotesViewModel.viewFavouriteQuote(it)
                                          },
                                          favouriteItemLongClicked = {
                                              // todo: share
                                          })
                    }
                }
            }

            PullRefreshIndicator(
                refreshing, pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}