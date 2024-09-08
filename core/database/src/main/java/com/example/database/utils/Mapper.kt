package com.example.database.utils

import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO

class Mapper {
    companion object {
        fun mapModeltoDBO(
            shopModel: CoolShopModel,

            ): CoolShopDBO {
            return CoolShopDBO(
                id = shopModel.id,
                title = shopModel.title,
                imgPath = shopModel.imgPath,
                price = shopModel.price,
                rate = shopModel.rate,
                isLiked = shopModel.isLiked,
                description = shopModel.description,
                category = shopModel.category
            )
        }

        fun mapModelDBOtoModel(shopDBO: CoolShopDBO): CoolShopModel = CoolShopModel(
            id = shopDBO.id,
            title = shopDBO.title,
            imgPath = shopDBO.imgPath,
            price = shopDBO.price,
            rate =shopDBO.rate,
            description = shopDBO.description,
            category = shopDBO.category,
            isLiked = shopDBO.isLiked
        )
    }
}
