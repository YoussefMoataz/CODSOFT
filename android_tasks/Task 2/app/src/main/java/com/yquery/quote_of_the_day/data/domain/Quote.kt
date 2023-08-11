package com.yquery.quote_of_the_day.data.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "_id") @SerializedName(value = "_id") val quoteID: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
)