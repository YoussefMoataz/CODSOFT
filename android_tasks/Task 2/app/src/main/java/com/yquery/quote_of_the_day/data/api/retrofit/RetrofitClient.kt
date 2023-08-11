package com.yquery.quote_of_the_day.data.api.retrofit

import com.yquery.quote_of_the_day.core.Constants.BASE_URL
import com.yquery.quote_of_the_day.data.api.QuotesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitQuotesClient {
    companion object {
        private var instance : QuotesApi? = null

        @Synchronized
        fun getInstance(): QuotesApi {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(QuotesApi::class.java)
            return instance as QuotesApi
        }
    }
}