package com.example.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
 data class CoolShopModel(
    val id: Int?,
    val title: String?,
    val imgPath: String?,
    val price: Double?,
    val rate: Double?,
    var isLiked:Boolean,
    val description:String? = null,
    val category:String? = null,
) : Parcelable


