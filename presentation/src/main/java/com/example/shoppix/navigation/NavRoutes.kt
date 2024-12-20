package com.example.shoppix.navigation

import com.example.shoppix.model.UiProductModel
import kotlinx.serialization.Serializable

 @Serializable
 object HomeScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen



@Serializable
 object CartScreen

@Serializable
object OrdersScreen


 @Serializable
 object ProfileScreen

  @Serializable
  object CartSummaryScreen

 @Serializable
 data class ProductDetails(val product: UiProductModel)

 @Serializable
 data class UserAddressRoute(val userAddressWrapper: UserAddressRouteWrapper)