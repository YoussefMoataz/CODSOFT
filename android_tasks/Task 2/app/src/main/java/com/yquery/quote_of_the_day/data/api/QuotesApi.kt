package com.yquery.quote_of_the_day.data.api

import com.yquery.quote_of_the_day.data.api.domain.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("/random")
    suspend fun getQuote(): Response<Quote>

}