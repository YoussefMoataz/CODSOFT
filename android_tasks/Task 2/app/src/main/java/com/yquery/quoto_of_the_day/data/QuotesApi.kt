package com.yquery.quoto_of_the_day.data

import com.yquery.quoto_of_the_day.domain.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("/random")
    suspend fun getQuote(): Response<Quote>

}