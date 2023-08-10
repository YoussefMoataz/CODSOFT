package com.yquery.quoto_of_the_day

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.yquery.quoto_of_the_day.presentation.QuotesViewModel
import com.yquery.quoto_of_the_day.retrofit.RetrofitQuotesClient
import com.yquery.quoto_of_the_day.ui.theme.QuotoOfTheDayTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quotesViewModel by viewModels<QuotesViewModel>()

        setContent {
            QuotoOfTheDayTheme {

                val quoteText by quotesViewModel.getQuoteText().collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(text = quoteText ?: "")
                    
                    Button(onClick = { quotesViewModel.refreshQuote() }) {
                        Text(text = "Refresh")
                    }
                }
            }
        }
    }
}
