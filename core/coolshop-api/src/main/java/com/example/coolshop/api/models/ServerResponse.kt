package com.example.coolshop.api.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
internal data class ServerResponse(
	@Json(name="Response")
	val response: List<ProductDTO?>? = null
)

@Serializable data class Rating(

	@Json(name="rate")
	val rate: Double? = null,

	@Json(name="count")
	val count: Int? = null
)

@Serializable
 data class ProductDTO(

	@Json(name="image")
	val image: String? = null,

	@Json(name="price")
	val price: Double? = null,

	@Json(name = "rating")
	val rating: Rating? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="category")
	val category: String? = null
)
