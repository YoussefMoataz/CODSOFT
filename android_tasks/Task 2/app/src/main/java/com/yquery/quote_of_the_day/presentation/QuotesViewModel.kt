package com.yquery.quote_of_the_day.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yquery.quote_of_the_day.data.api.domain.Quote
import com.yquery.quote_of_the_day.data.api.retrofit.RetrofitQuotesClient
import com.yquery.quote_of_the_day.data.persistent.QuotesDao
import com.yquery.quote_of_the_day.data.persistent.domain.FavouriteQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(private val quotesDao: QuotesDao) : ViewModel() {

    private val quotesApi = RetrofitQuotesClient.getInstance()

    private val _currentQuote = MutableStateFlow<Quote?>(null)
    private val favourites = MutableStateFlow<List<FavouriteQuote>>(emptyList())

    fun getQuote(): StateFlow<Quote?> {

        if (_currentQuote.value == null) {
            viewModelScope.launch {
                val response = quotesApi.getQuote()
                if (response.isSuccessful) {
                    _currentQuote.update {
                        response.body()
                    }
                    response.body()?.let {
                        quotesDao.addToFavourites(
                            FavouriteQuote(
                                it.id,
                                it.author,
                                it.content
                            )
                        )
                    }
                }
            }
        }

        return _currentQuote.asStateFlow()

    }

    fun refreshQuote() {
        _currentQuote.value = null
        getQuote()
        getFavourites()
    }

    fun getFavourites(): StateFlow<List<FavouriteQuote>?> {

        viewModelScope.launch {
            favourites.value = quotesDao.getFavourites()
        }

        return favourites.asStateFlow()

    }

}