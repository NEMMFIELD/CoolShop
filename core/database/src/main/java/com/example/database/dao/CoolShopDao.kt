package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.models.CoolShopDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface CoolShopDao {
    @Query("SELECT * FROM CoolShopProducts")
    suspend fun getAll(): List<CoolShopDBO>

    @Query("SELECT * FROM CoolShopProducts")
    fun observeAll(): Flow<List<CoolShopDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: CoolShopDBO)

    @Delete
    suspend fun remove(product: CoolShopDBO)

    @Query("DELETE FROM CoolShopProducts")
    suspend fun clean()
}
