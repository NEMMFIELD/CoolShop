package com.example.coolshop.cart.data

import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO

class CartMapper {
    companion object {

        fun mapModelToDBO(
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

        fun mapModelDBOToModel(shopDBO: CoolShopDBO): CoolShopModel = CoolShopModel(
            id = shopDBO.id,
            title = shopDBO.title,
            imgPath = shopDBO.imgPath,
            price = shopDBO.price,
            rate = shopDBO.rate,
            description = shopDBO.description,
            category = shopDBO.category,
            isLiked = shopDBO.isLiked
        )

    }
}
