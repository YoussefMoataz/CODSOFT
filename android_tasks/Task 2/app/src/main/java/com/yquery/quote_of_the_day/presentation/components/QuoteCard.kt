package com.yquery.quote_of_the_day.presentation.components

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.dp
import com.yquery.quote_of_the_day.core.Constants
import com.yquery.quote_of_the_day.core.ShareUtils
import com.yquery.quote_of_the_day.data.domain.Quote
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController

@Composable
fun QuoteCard(
    quote: Quote?,
    isFavourite: Boolean,
    quoteShare: () -> Unit,
    quoteToggleFavourite: () -> Unit,
    context: Context,
    captureController: CaptureController
) {

    val textColor = if (quote?.id == Constants.CONNECTION_ERROR_ID) {
        MaterialTheme.colorScheme.error
    } else {
        Color.Unspecified
    }

    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = SpringSpec(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Capturable(
            controller = captureController,
            onCaptured = { bitmap, error ->
                // This is captured bitmap of a content inside Capturable Composable.
                if (bitmap != null) {
                    ShareUtils.shareImageToOthers(context, "", bitmap.asAndroidBitmap())
                }

                if (error != null) {
                    println("error onCaptured")
                }
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                AnimatedContent(targetState = quote?.content ?: "",
                                label = "",
                                transitionSpec = {
                                    fadeIn(animationSpec = tween(300)) togetherWith
                                            fadeOut(animationSpec = tween(300))
                                }) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(14.dp),
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        lineHeight = MaterialTheme.typography.headlineMedium.lineHeight,
                        color = textColor,
                    )
                }

                Text(
                    text = "-${quote?.author}" ?: "",
                    modifier = Modifier
                        .padding(end = 14.dp, top = 4.dp, bottom = 6.dp)
                        .align(Alignment.End),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = textColor
                )
            }
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