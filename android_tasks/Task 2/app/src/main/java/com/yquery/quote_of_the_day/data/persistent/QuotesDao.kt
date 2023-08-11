package com.yquery.quote_of_the_day.data.persistent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yquery.quote_of_the_day.data.domain.Quote

@Dao
interface QuotesDao {

    @Query("SELECT _id FROM Quote")
    suspend fun getFavouritesIDs(): List<String>

    @Query("SELECT * FROM Quote ORDER BY id ASC")
    suspend fun getFavourites(): List<Quote>

    @Insert
    suspend fun addToFavourites(quote: Quote)

    @Query("DELETE FROM Quote WHERE _id = :id")
    suspend fun removeFromFavourites(id: String)

}