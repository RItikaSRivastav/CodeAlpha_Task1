package com.example.shoppix.ui.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.OrdersData
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.OrderListUseCase
import com.example.shoppix.ShoppixSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val orderListUseCase: OrderListUseCase
) : ViewModel() {

    private val _ordersEvent = MutableStateFlow<OrdersEvents>(OrdersEvents.Loading)
    val ordersEvents = _ordersEvent.asStateFlow()
    val userDomainModel = ShoppixSession.getUser()

  init {
      getOrderList()
  }

    fun filterOrders(list:List<OrdersData>, filters: String):List<OrdersData>{
        val filteredList = list.filter { it.status == filters }
        return filteredList
    }

   private fun getOrderList() {
        viewModelScope.launch {
            val result = orderListUseCase.execute(userDomainModel!!.id!!.toLong())

            when (result){
                is ResultWrapper.Success -> {
                    val data = result.value
                    _ordersEvent.value = OrdersEvents.Success(data.data)
                }
                is ResultWrapper.Failure -> {
                    _ordersEvent.value = OrdersEvents.Error("Something went Wrong")
                }
            }
        }
    }
}

 sealed class OrdersEvents {
     object Loading: OrdersEvents()
     data class Success(val data:List<OrdersData>): OrdersEvents()
     data class Error(val errormsg: String): OrdersEvents()
 }