package com.yquery.quote_of_the_day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yquery.quote_of_the_day.presentation.QuotesViewModel
import com.yquery.quote_of_the_day.ui.theme.QuotoOfTheDayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuotoOfTheDayTheme {

                val quotesViewModel = hiltViewModel<QuotesViewModel>()
                val quote by quotesViewModel.getQuote().collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text(text = quote?.content ?: "")

                        Button(onClick = { quotesViewModel.refreshQuote() }) {
                            Text(text = "Refresh")
                        }
                    }
                }
            }
        }
    }
}
