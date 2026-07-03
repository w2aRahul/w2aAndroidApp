package com.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserProfile(
    val name: String = "Rahul Arora",
    val email: String = "trainer@way2automation.com",
    val phone: String = "+1 (555) 019-2834"
)

data class Address(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fullName: String,
    val mobileNumber: String,
    val houseInfo: String,
    val city: String,
    val state: String,
    val pincode: String
)

class MediShopViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateProfile(name: String, email: String, phone: String) {
        _userProfile.value = UserProfile(name, email, phone)
    }

    private val _savedAddresses = MutableStateFlow<List<Address>>(emptyList())
    val savedAddresses: StateFlow<List<Address>> = _savedAddresses.asStateFlow()

    fun addAddress(address: Address) {
        _savedAddresses.update { it + address }
    }
    
    fun removeAddress(id: String) {
        _savedAddresses.update { it.filter { addr -> addr.id != id } }
    }

    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(sampleProducts[1], 1), // Amoxicillin
            CartItem(sampleProducts[0].copy(name = "Ibuprofen 200mg", price = 4.24, imageResUrl = ""), 2) // Just faking Ibuprofen for cart
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            if (existingItem != null) {
                currentList.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else {
                currentList + CartItem(product, quantity)
            }
        }
    }

    fun removeCartItem(productId: String) {
        _cartItems.update { current ->
            current.filter { it.product.id != productId }
        }
    }

    fun updateCartItemQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeCartItem(productId)
            return
        }
        _cartItems.update { current ->
            current.map {
                if (it.product.id == productId) it.copy(quantity = quantity)
                else it
            }
        }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
