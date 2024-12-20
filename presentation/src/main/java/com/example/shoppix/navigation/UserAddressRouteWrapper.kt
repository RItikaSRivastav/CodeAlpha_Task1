package com.example.shoppix.navigation

import android.os.Parcelable
import com.example.shoppix.model.UserAddress
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddressRouteWrapper(
    val userAddress: UserAddress?
): Parcelable
