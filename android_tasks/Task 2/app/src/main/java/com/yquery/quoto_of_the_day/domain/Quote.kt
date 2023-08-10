package com.yquery.quoto_of_the_day.domain

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("_id")
    val id: String,
    val author: String,
    val content: String,
)