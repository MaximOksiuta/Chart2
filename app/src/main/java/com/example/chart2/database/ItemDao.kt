package com.example.chart2.database

import androidx.room.*
import com.example.chart2.models.RVItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item): Long

    @Update
    suspend fun updateState(item: Item)

    @Query("SELECT * FROM Item Where id=:id")
    suspend fun getItemById(id: Int): Item

    @Query("SELECT id, category, name as 'description', price, state FROM Item Where date = :pickDate")
    fun getAllWithTime(pickDate: Long): Flow<List<RVItemData>>

    @Query("SELECT * FROM Item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT date, SUM(price) as 'sumPrice' FROM Item WHERE date BETWEEN :fromDate AND :toDate GROUP BY date")
    fun getAllInRangeWithSum(fromDate: Long, toDate: Long): Flow<List<GraphicsDataCount>>

    @Query("SELECT category as type, SUM(price) as 'sum' FROM Item WHERE date BETWEEN :fromDate AND :toDate GROUP BY type")
    fun getAllTypesInRange(fromDate: Long, toDate: Long): Flow<List<GraphicsDataType>>

}