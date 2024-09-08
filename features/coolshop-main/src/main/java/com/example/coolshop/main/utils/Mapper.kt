package com.example.coolshop.main.utils

import com.example.coolshop.api.models.ResponseItem
import com.example.data.CoolShopModel

 class Mapper {
    companion object {
        fun mapDTOtoModel(
            shopDTO: ResponseItem,

        ): CoolShopModel {
            return CoolShopModel(
                id = shopDTO.id,
                title = shopDTO.title,
                imgPath = shopDTO.image,
                price = shopDTO.price,
                rate = shopDTO.rating?.rate,
                isLiked = false,
                description = shopDTO.description,
                category = shopDTO.category
            )
        }
    }
}
