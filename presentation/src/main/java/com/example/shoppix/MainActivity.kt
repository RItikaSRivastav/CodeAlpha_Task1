package com.example.shoppix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.shoppix.model.UiProductModel
import com.example.shoppix.navigation.CartScreen
import com.example.shoppix.navigation.CartSummaryScreen
import com.example.shoppix.navigation.HomeScreen
import com.example.shoppix.navigation.LoginScreen
import com.example.shoppix.navigation.OrdersScreen
import com.example.shoppix.navigation.ProductDetails
import com.example.shoppix.navigation.ProductNavType
import com.example.shoppix.navigation.ProfileScreen
import com.example.shoppix.navigation.RegisterScreen
import com.example.shoppix.navigation.UserAddressRoute
import com.example.shoppix.navigation.UserAddressRouteWrapper
import com.example.shoppix.navigation.userAddressNavType
import com.example.shoppix.ui.feature.account.login.LoginScreen
import com.example.shoppix.ui.feature.account.register.RegisterScreen
import com.example.shoppix.ui.feature.cart.CartScreen
import com.example.shoppix.ui.feature.home.HomeScreen
import com.example.shoppix.ui.feature.orders.OrdersScreen
import com.example.shoppix.ui.feature.product_details.ProductDetailsScreen
import com.example.shoppix.ui.feature.summary.CartSummaryScreen
import com.example.shoppix.ui.feature.user_address.UserAddressScreen
import com.example.shoppix.ui.theme.ShoppixTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppixTheme {
                val shouldShowBottomNav = remember {
                    mutableStateOf(true)
                }
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn() ) {
                            BottomNavigationBar(navController)
                        }

                    }
                ) {
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                    ) {

                        NavHost(navController = navController, startDestination = if (ShoppixSession.getUser() !=null) {
                            HomeScreen
                        }else {
                            LoginScreen
                        }
                        ) {

                            composable<LoginScreen> {
                                shouldShowBottomNav.value = false
                                LoginScreen(navController)
                            }
                            composable<RegisterScreen> {
                                shouldShowBottomNav.value = false
                                RegisterScreen(navController)
                            }
                            composable<HomeScreen> {
                                HomeScreen(navController)
                                shouldShowBottomNav.value = true
                            }
                            composable<CartScreen> {
                                shouldShowBottomNav.value = true
                              CartScreen(navController)
                            }
                            composable<OrdersScreen> {
                                shouldShowBottomNav.value = true
                                OrdersScreen()
                            }
                            composable<ProfileScreen> {
                                shouldShowBottomNav.value = true
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "profile")
                                }
                            }
                            composable<CartSummaryScreen> {
                                shouldShowBottomNav.value = false
                                CartSummaryScreen(navController = navController)
                            }
                            composable<ProductDetails>(
                                typeMap = mapOf(typeOf<UiProductModel>() to ProductNavType)
                            ) {
                                shouldShowBottomNav.value = false
                                val productRoute = it.toRoute<ProductDetails>()
                               ProductDetailsScreen(navController, productRoute.product )
                            }
                            composable<UserAddressRoute>(
                                typeMap = mapOf(typeOf<UserAddressRouteWrapper>() to userAddressNavType)
                            ) {
                                shouldShowBottomNav.value = false
                                val userAddressRoute = it.toRoute<UserAddressRoute>()
                                UserAddressScreen(navController = navController,
                                    userAddress = userAddressRoute.userAddressWrapper.userAddress
                                )
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController){
    NavigationBar {
        //current route
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val items = listOf(
            BottomNavItems.Home,
            BottomNavItems.Oders,
            BottomNavItems.Profile
        )
        items.forEach { item ->
            val isSelected = currentRoute?.substringBefore("?") == item.route::class.qualifiedName
            NavigationBarItem(selected = isSelected,
                onClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let {  startRoute ->
                            popUpTo(startRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = item.title)},
                icon = {
                Image(painter = painterResource(id = item.icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray)
                  )
                }, colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}


 sealed class BottomNavItems(val route: Any, val title:String, val icon: Int){
     object Home : BottomNavItems(HomeScreen, "Home", icon = R.drawable.baseline_home_24)
     object Oders : BottomNavItems(OrdersScreen, "Order", icon = R.drawable.baseline_checklist_rtl_24)
     object Profile : BottomNavItems(ProfileScreen, "Profile", icon = R.drawable.baseline_person_24)
 }
