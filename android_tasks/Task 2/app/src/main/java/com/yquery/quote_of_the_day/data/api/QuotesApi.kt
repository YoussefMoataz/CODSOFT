package com.yquery.quote_of_the_day.data.api

import com.yquery.quote_of_the_day.data.domain.Quote
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {

    @GET("/random")
    fun getQuote(@Query("maxLength") maxLength: Int = 150): Call<Quote>

}