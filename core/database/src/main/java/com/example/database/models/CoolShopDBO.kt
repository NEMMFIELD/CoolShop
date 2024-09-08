package com.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CoolShopProducts")
class CoolShopDBO(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo("title") val title: String?,
    @ColumnInfo("urlToImage") val imgPath: String?,
    @ColumnInfo("price") val price: Double?,
    @ColumnInfo("rating") val rate: Double?,
    @ColumnInfo("favourite") var isLiked: Boolean,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("category") val category: String?,
)
