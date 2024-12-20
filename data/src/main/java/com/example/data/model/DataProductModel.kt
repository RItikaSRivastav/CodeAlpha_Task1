package com.example.data.model

import kotlinx.serialization.Serializable


@Serializable
class DataProductModel (
    val id: Int,
    val title: String,
    val price: Double,
    val categoryId: Int,
    val description: String,
    val image: String
    ) {
        fun  toProduct() = com.example.domain.model.Product(
            id = id,
            title = title,
            price = price,
            categoryId = categoryId,
            description = description,
            image = image
        )

    }
