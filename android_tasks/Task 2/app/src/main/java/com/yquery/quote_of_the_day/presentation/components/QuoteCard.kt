package com.yquery.quote_of_the_day.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yquery.quote_of_the_day.data.domain.Quote

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteCard(quote: Quote?, quoteCardLongClicked: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(10.dp, 36.dp)
            .fillMaxWidth()
            .combinedClickable(onClick = {},
                               onLongClick = { quoteCardLongClicked() })
    ) {

        Text(
            text = quote?.content ?: "",
            modifier = Modifier.padding(14.dp),
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )

        Text(
            text = quote?.author ?: "",
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.End),
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )

    }

}