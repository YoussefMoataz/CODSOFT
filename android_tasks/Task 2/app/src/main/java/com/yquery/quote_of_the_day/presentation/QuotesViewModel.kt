package com.yquery.quote_of_the_day.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yquery.quote_of_the_day.data.api.retrofit.RetrofitQuotesClient
import com.yquery.quote_of_the_day.data.domain.Quote
import com.yquery.quote_of_the_day.data.persistent.QuotesDao
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
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _favourites = MutableStateFlow<List<Quote>>(emptyList())
    private val _favouriteState = MutableStateFlow(false)

    fun getQuote(): StateFlow<Quote?> {

        if (_currentQuote.value == null) {

            _isRefreshing.value = true

            viewModelScope.launch {
                try {
                    val response = quotesApi.getQuote()
                    if (response.isSuccessful) {
                        _currentQuote.update {
                            response.body()
                        }
                    }
                    _isRefreshing.value = false
                }catch (_: Exception){
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

    fun getFavourites(): StateFlow<List<Quote>?> {

        viewModelScope.launch {
            _favourites.value = quotesDao.getFavourites()
        }

        return _favourites.asStateFlow()
    }

    fun changeFavouriteStatus(quote: Quote) {

        viewModelScope.launch {
            if (quote.quoteID in quotesDao.getFavouritesIDs()) {
                quotesDao.removeFromFavourites(quote.quoteID)
            } else {
                quotesDao.addToFavourites(quote)
            }
            isFavourite(quote)
            getFavourites()
        }

    }

    fun isFavourite(quote: Quote?): StateFlow<Boolean> {

        viewModelScope.launch {
            _favouriteState.value = (quote?.quoteID in quotesDao.getFavouritesIDs())
        }

        return _favouriteState.asStateFlow()
    }

    fun viewFavouriteQuote(quote: Quote){
        _currentQuote.value = quote
    }

}