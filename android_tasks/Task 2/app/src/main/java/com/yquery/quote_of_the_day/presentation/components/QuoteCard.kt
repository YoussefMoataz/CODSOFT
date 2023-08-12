package com.yquery.quote_of_the_day.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yquery.quote_of_the_day.core.Constants
import com.yquery.quote_of_the_day.data.domain.Quote

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteCard(
    quote: Quote?,
    isFavourite: Boolean,
    quoteShare: () -> Unit,
    quoteToggleFavourite: () -> Unit
) {

    val textColor = if (quote?.id == Constants.CONNECTION_ERROR_ID) {
        MaterialTheme.colorScheme.error
    } else {
        Color.Unspecified
    }

    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Card(colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )) {
            Text(
                text = quote?.content ?: "",
                modifier = Modifier.padding(14.dp),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                lineHeight = MaterialTheme.typography.headlineMedium.lineHeight,
                color = textColor
            )

            Text(
                text = "-${quote?.author}" ?: "",
                modifier = Modifier
                    .padding(end = 14.dp, top = 4.dp, bottom = 2.dp)
                    .align(Alignment.End),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = textColor
            )
        }

        if (quote?.id !in Constants.errorList) {
            Row(
                modifier = Modifier.padding(start = 4.dp, top = 0.dp, end = 0.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(modifier = Modifier.padding(4.dp, 0.dp), onClick = { quoteShare() }) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share"
                    )
                }

                IconButton(
                    modifier = Modifier.padding(4.dp, 0.dp),
                    onClick = { quoteToggleFavourite() }) {
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
        }

    }

}