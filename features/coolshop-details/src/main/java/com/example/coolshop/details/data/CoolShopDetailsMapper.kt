package com.example.coolshop.details.data

import com.example.coolshop.api.models.ProductDTO
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO

class CoolShopDetailsMapper {
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
    }
}
