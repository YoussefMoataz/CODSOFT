package com.yquery.quoto_of_the_day.presentation

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.yquery.quoto_of_the_day.retrofit.RetrofitQuotesClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuotesViewModel: ViewModel() {

    private val quotesApi = RetrofitQuotesClient.getInstance()

    private val _quoteText = MutableStateFlow<String?>(null)

    fun getQuoteText(): StateFlow<String?> {

        if (_quoteText.value == null) {
            viewModelScope.launch {
                val response = quotesApi.getQuote()
                if (response.isSuccessful) {
                    _quoteText.update {
                        response.body()?.content
                    }
                }
            }
        }

        return _quoteText.asStateFlow()

    }

    fun refreshQuote(){
        _quoteText.value = null
        getQuoteText()
    }

}