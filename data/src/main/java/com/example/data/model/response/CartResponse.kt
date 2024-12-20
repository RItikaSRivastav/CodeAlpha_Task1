package com.example.data.model.response

import com.example.domain.model.CartItemModel
import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val data: List<CartItem>,
    val msg: String
 ){
    fun toCartModel(): com.example.domain.model.CartModel {
        return com.example.domain.model.CartModel(
            data = data.map { it.toCartItemModel() },
            msg = msg
        )
    }
}