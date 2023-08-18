package com.yquery.quote_of_the_day.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yquery.quote_of_the_day.R
import com.yquery.quote_of_the_day.presentation.components.FavouriteListItem
import com.yquery.quote_of_the_day.presentation.components.QuoteCard
import dev.shreyaspatil.capturable.controller.rememberCaptureController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {

    val quotesViewModel = hiltViewModel<QuotesViewModel>()
    val quote by quotesViewModel.getQuote().collectAsState()
    val isFavourite = quotesViewModel.isFavourite(quote).collectAsState().value
    val quotesFavourites = quotesViewModel.getFavourites().collectAsState()

//    val refreshing by quotesViewModel.isRefreshing.collectAsState()
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = refreshing,
//        onRefresh = { quotesViewModel.refreshQuote() })

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
//                                   actions = {
//                                       IconButton(onClick = { quotesViewModel.refreshQuote() }) {
//                                           Icon(
//                                               imageVector = Icons.Rounded.Refresh,
//                                               contentDescription = "Refresh Quote"
//                                           )
//                                       }
//                                   }
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                quote?.let { quotesViewModel.changeFavouriteStatus(it) }
//            }) {
//                Icon(
//                    imageVector = if (isFavourite) {
//                        Icons.Filled.Favorite
//                    } else {
//                        Icons.Outlined.FavoriteBorder
//                    },
//                    contentDescription = "Favourite"
//                )
//            }
//        }
    ) { paddings ->

        Box(
            modifier = Modifier
                .padding(paddings)
//                .pullRefresh(pullRefreshState)
        ) {

            Column {

                Spacer(modifier = Modifier.height(Dp(10F)))

                QuoteCard(
                    quote = quote, isFavourite = isFavourite,
                    quoteShareText = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${quote?.content}\n-${quote?.author}"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    },
                    quoteShareImage = {
                        captureController.capture()
                    },
                    quoteToggleFavourite = {
                        quote?.let { quotesViewModel.changeFavouriteStatus(it) }
                    },
                    context = context,
                    captureController = captureController
                )


                Spacer(modifier = Modifier.height(Dp(8F)))
                Divider()

                LazyColumn {
                    items(items = quotesFavourites.value ?: emptyList()) {
                        FavouriteListItem(quote = it,
                                          favouriteItemClicked = {
                                              quotesViewModel.viewFavouriteQuote(it)
                                          })
                    }
                }
            }

//            PullRefreshIndicator(
//                refreshing, pullRefreshState,
//                Modifier.align(Alignment.TopCenter)
//            )
        }
    }
}