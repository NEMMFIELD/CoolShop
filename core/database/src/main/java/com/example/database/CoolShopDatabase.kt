package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO

class CoolShopDatabase internal constructor(private val database: CoolShopRoomDatabase) {
    val coolShopDao: CoolShopDao
        get() = database.coolShopDao()
}

@Database(entities = [CoolShopDBO::class], version = 1, exportSchema = false)
abstract class CoolShopRoomDatabase : RoomDatabase() {
    abstract fun coolShopDao(): CoolShopDao
}

fun CoolShopDatabase(applicationContext: Context): CoolShopDatabase {
    val coolShopRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        CoolShopRoomDatabase::class.java,
        "coolShop"
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
    return CoolShopDatabase(coolShopRoomDatabase)
}
