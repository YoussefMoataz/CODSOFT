package com.yquery.quote_of_the_day.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yquery.quote_of_the_day.core.Constants
import com.yquery.quote_of_the_day.data.api.retrofit.RetrofitQuotesClient
import com.yquery.quote_of_the_day.data.domain.Quote
import com.yquery.quote_of_the_day.data.persistent.QuotesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

            _currentQuote.value = Quote(
                id = Constants.REFRESHING_QUOTE_ID,
                quoteID = "",
                author = "Quote Of The Day",
                content = "Loading Quote..."
            )

            _isRefreshing.value = true

            viewModelScope.launch {
                try {
//                    val response = quotesApi.getQuote()
//                    if (response.isSuccessful) {
//                        _currentQuote.update {
//                            response.body()
//                        }
//                    }

                    quotesApi.getQuote().enqueue(object : Callback<Quote> {
                        override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                            print(response.body())
                            _currentQuote.update {
                                response.body()
                            }
                        }

                        override fun onFailure(call: Call<Quote>, t: Throwable) {
                            _currentQuote.value = Quote(
                                id = Constants.CONNECTION_ERROR_ID,
                                quoteID = "",
                                author = "Quote Of The Day",
                                content = "Failed to load quote! Please check your Internet connection."
                            )
                        }

                    })

                    _isRefreshing.value = false
                } catch (_: Exception) {
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

    fun viewFavouriteQuote(quote: Quote) {
        _currentQuote.value = quote
    }

}