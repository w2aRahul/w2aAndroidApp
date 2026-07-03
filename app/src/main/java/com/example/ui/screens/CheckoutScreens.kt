package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.CartItem
import com.example.MediShopViewModel
import com.example.Product

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CartScreen(navController: NavController, viewModel: MediShopViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val tax = subtotal * 0.08
    val total = subtotal + tax

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = { AppBottomNavigation(navController, viewModel) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .semantics { testTagsAsResourceId = true }
        ) {
            
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty.", style = MaterialTheme.typography.titleLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.updateCartItemQuantity(item.product.id, item.quantity + 1) },
                            onDecrease = { viewModel.updateCartItemQuantity(item.product.id, item.quantity - 1) },
                            onRemove = { viewModel.removeCartItem(item.product.id) }
                        )
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).padding(bottom = 80.dp) // extra padding for bottom nav
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text("Order Summary", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)) {
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = {},
                                        placeholder = { Text("Promo code", style = MaterialTheme.typography.bodyMedium) },
                                        modifier = Modifier.weight(1f).height(52.dp),
                                        shape = RoundedCornerShape(50),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}, shape = RoundedCornerShape(50), modifier = Modifier.height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
                                        Text("Apply")
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(String.format("$%.2f", subtotal), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Shipping", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Calculated at checkout", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Tax", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(String.format("$%.2f", tax), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Total", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold))
                                    Text(String.format("$%.2f", total), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = { navController.navigate("checkout") },
                                    modifier = Modifier.fillMaxWidth().height(56.dp).testTag("cart_checkout_button"),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Secure Checkout", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                if (item.product.imageResUrl.isNotEmpty()) {
                    AsyncImage(
                        model = item.product.imageResUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        contentScale = ContentScale.Inside
                    )
                } else {
                    Icon(Icons.Filled.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.Center).size(36.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.product.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(item.product.formFactor.takeIf { it.isNotBlank() } ?: "Count - Various", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(String.format("$%.2f", item.product.price), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(50)).padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) { Icon(Icons.Filled.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp)) }
                        Text("${item.quantity}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))) { Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp)) }
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Filled.DeleteOutline, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CheckoutScreen(navController: NavController, viewModel: MediShopViewModel) {
    var fullName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var house by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    
    var paymentMethod by remember { mutableStateOf("card") }

    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val tax = subtotal * 0.08
    val total = subtotal + tax

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Icon(Icons.Filled.Lock, contentDescription = "Secure", tint = MaterialTheme.colorScheme.primary.copy(alpha=0.5f), modifier = Modifier.padding(end=16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Amount", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(String.format("$%.2f", total), style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                    Button(
                        onClick = {
                            viewModel.clearCart()
                            navController.navigate("orderConfirmation") {
                                popUpTo("home") { inclusive = false }
                            }
                        },
                        modifier = Modifier.width(180.dp).height(56.dp).testTag("checkout_place_order_button"),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Place Order", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth().padding(top=16.dp)
                ) {
                    Row(modifier = Modifier.padding(24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Order Total", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                                Text("${cartItems.sumOf { it.quantity }} Items", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(String.format("$%.2f", total), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text("View Details", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                    Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Delivery Address", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                }
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                        OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth().testTag("checkout_mobile_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                        OutlinedTextField(value = house, onValueChange = { house = it }, label = { Text("House No., Building, Street, Area") }, modifier = Modifier.fillMaxWidth().testTag("checkout_house_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f).testTag("checkout_city_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                            OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.weight(1f).testTag("checkout_state_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                        }
                        OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.fillMaxWidth().testTag("checkout_pincode_input"), singleLine = true, shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.outlineVariant, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant))
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                    Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Payment Method", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                }
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PaymentOption(selected = paymentMethod == "card", onClick = { paymentMethod = "card" }, icon = Icons.Filled.CreditCard, title = "Credit / Debit Card", subtitle = "Secure gateway")
                        PaymentOption(selected = paymentMethod == "upi", onClick = { paymentMethod = "upi" }, icon = Icons.Filled.QrCodeScanner, title = "UPI App", subtitle = "Google Pay, PhonePe, Paytm")
                        PaymentOption(selected = paymentMethod == "cod", onClick = { paymentMethod = "cod" }, icon = Icons.Filled.Payments, title = "Cash on Delivery", subtitle = "Pay when you receive")
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(selected: Boolean, onClick: () -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary))
            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = icon, contentDescription = null, tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(navController: NavController) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(innerPadding).padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(100)))
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(100)))
                    Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text("Order Confirmed", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Your prescription order has been securely received and is being processed by our pharmacy team.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.width(320.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                            Row {
                                Icon(Icons.Filled.Receipt, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Order ID", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("#RX-8492-MED", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                            Row {
                                Icon(Icons.Filled.Payments, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Total Amount", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("$142.50", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { navController.navigate("orders") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(Icons.Filled.History, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Track Order Status", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Icon(Icons.Filled.Storefront, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Return to Pharmacy", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
