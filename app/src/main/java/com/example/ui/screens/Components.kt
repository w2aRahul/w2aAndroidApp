package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.example.MediShopViewModel

sealed class BottomNavItem(val route: String, val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Cart : BottomNavItem("cart", "Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    object Orders : BottomNavItem("orders", "Orders", Icons.Filled.Receipt, Icons.Outlined.Receipt)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun AppBottomNavigation(navController: NavController, viewModel: MediShopViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val cartItemCount = cartItems.sumOf { it.quantity }
    
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.testTag("app_bottom_nav"),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = { 
                    if (item == BottomNavItem.Cart && cartItemCount > 0) {
                        BadgedBox(badge = { Badge { Text(cartItemCount.toString()) } }) {
                            Icon(
                                imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                contentDescription = item.title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.title
                        ) 
                    }
                },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                alwaysShowLabel = true,
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
