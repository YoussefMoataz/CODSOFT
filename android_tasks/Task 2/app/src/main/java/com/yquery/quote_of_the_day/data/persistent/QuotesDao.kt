package com.yquery.quote_of_the_day.data.persistent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yquery.quote_of_the_day.data.persistent.domain.FavouriteQuote

@Dao
interface QuotesDao {

    @Query("SELECT * FROM FavouriteQuote")
    suspend fun getFavourites(): List<FavouriteQuote>

    @Insert
    suspend fun addToFavourites(quote: FavouriteQuote)

    @Query("DELETE FROM FavouriteQuote WHERE _id = :id")
    suspend fun removeFromFavourites(id: String)

}