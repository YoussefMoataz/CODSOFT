package com.yquery.quote_of_the_day.data.api.domain

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("_id") val id: String,
    val author: String,
    val content: String,
)