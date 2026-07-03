package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MediShopApp()
                }
            }
        }
    }
}

@Composable
fun MediShopApp() {
    val navController = rememberNavController()
    val viewModel: MediShopViewModel = viewModel()
    
    // Simple state flow for dark mode to demonstrate settings change
    // Using simple boolean state, though ideally it should come from a store.
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable(
            "productList?category={category}",
            arguments = listOf(androidx.navigation.navArgument("category") {
                defaultValue = ""
                type = androidx.navigation.NavType.StringType
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductListScreen(navController, viewModel, category)
        }
        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: "p1"
            ProductDetailScreen(navController, viewModel, productId)
        }
        composable("cart") { CartScreen(navController, viewModel) }
        composable("checkout") { CheckoutScreen(navController, viewModel) }
        composable("orderConfirmation") { OrderConfirmationScreen(navController) }
        composable("orders") { OrdersScreen(navController, viewModel) }
        composable("orderDetails/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: "10492"
            OrderDetailsScreen(navController, orderId)
        }
        composable("profile") { ProfileScreen(navController, viewModel) }
        composable("editProfile") { EditProfileScreen(navController, viewModel) }
        composable("deliveryAddresses") { DeliveryAddressesScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController) }
        composable("hydration") { HydrationScreen(navController) }
    }
}

