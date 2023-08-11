package com.yquery.quote_of_the_day.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yquery.quote_of_the_day.data.domain.Quote

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavouriteListItem(
    quote: Quote,
    favouriteItemClicked: () -> Unit,
) {

//    ElevatedCard(
//        modifier = Modifier
//            .padding(18.dp)
//            .fillMaxWidth()
//            .clickable { favouriteItemClicked() }
//    ) {
//
//        Text(
//            text = quote.author,
//            modifier = Modifier.padding(8.dp),
//            fontSize = MaterialTheme.typography.titleSmall.fontSize
//        )
//
//        Text(
//            text = quote.content,
//            modifier = Modifier
//                .padding(8.dp),
//            fontSize = MaterialTheme.typography.bodySmall.fontSize,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//
//
//    }

    androidx.compose.material3.ListItem(modifier = Modifier
        .clickable { favouriteItemClicked() },
                                        headlineText = {
                                            Text(
                                                text = quote.author,
                                            )
                                        },
                                        supportingText = {
                                            Text(
                                                text = quote.content,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        })

    Divider()

}