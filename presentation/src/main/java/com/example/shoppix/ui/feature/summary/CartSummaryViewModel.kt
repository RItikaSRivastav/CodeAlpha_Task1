package com.example.shoppix.ui.feature.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CartSummary
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.CartSummaryUseCase
import com.example.domain.usecase.PlaceOrderUseCase
import com.example.shoppix.ShoppixSession
import com.example.shoppix.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartSummaryViewModel(
    private val cartSummaryUseCase: CartSummaryUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<CartSummaryEvent>(CartSummaryEvent.Loading)
    val uiState = _uiState.asStateFlow()
    val userDomainModel = ShoppixSession.getUser()

    init {
       getCartSummary( userDomainModel!!.id!!.toLong())
    }

  private  fun getCartSummary(userId: Long) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            val summary = cartSummaryUseCase.execute(userId)
            when (summary) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartSummaryEvent.Success(summary.value)
                }
                is ResultWrapper.Failure -> {
                    _uiState.value = CartSummaryEvent.Error("Something went wrong!")
                }
            }
        }
    }
    public fun placeOrder(userAddress: UserAddress){
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            val orderId = placeOrderUseCase.execute(userAddress.toAddressDataModel(),userDomainModel!!.id!!.toLong())
            when (orderId) {
                is ResultWrapper.Success -> {
                    _uiState.value = CartSummaryEvent.PlaceOrder(orderId.value)
                }

                is ResultWrapper.Failure -> {
                    _uiState.value = CartSummaryEvent.Error("Something went wrong!")
                }
            }
        }
    }
}

sealed class CartSummaryEvent {
    data object Loading : CartSummaryEvent()
    data class Error(val error: String) : CartSummaryEvent()
    data class Success(val summary: CartSummary) : CartSummaryEvent()
    data class PlaceOrder(val orderId: Long) : CartSummaryEvent()
}