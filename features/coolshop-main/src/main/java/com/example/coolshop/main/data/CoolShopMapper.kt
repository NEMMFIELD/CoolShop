package com.example.coolshop.main.data

import com.example.coolshop.api.models.ProductDTO
import com.example.data.CoolShopModel

class CoolShopMapper {

    companion object {
        fun mapDTOToModel(
            shopDTO: ProductDTO,
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
