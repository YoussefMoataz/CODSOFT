package com.yquery.quote_of_the_day.data.persistent.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteQuote(
    @PrimaryKey @ColumnInfo(name = "_id") val id: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
)