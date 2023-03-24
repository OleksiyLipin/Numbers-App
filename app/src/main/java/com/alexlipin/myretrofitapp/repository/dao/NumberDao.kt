package com.alexlipin.myretrofitapp.repository.dao

import androidx.room.*
import com.alexlipin.myretrofitapp.model.FactItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberDao {

    @Query("SELECT * FROM number_table")
    fun getItems(): Flow<List<FactItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(factItem: FactItem)

    @Query("SELECT * FROM number_table WHERE id=:id ")
    fun getNumberById(id: String): Flow<FactItem>
}