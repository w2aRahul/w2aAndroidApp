package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import android.widget.Toast
import coil.compose.AsyncImage
import com.example.Order
import com.example.OrderStatus
import com.example.sampleOrders
import com.example.MediShopViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController, viewModel: MediShopViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Orders", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }, modifier = Modifier.padding(end = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
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
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Order History", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                OutlinedButton(onClick = {}, shape = RoundedCornerShape(50), border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                    Text("Filter", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(sampleOrders) { order ->
                    OrderCard(order = order, onClick = { navController.navigate("orderDetails/${order.id}") })
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    val (statusText, statusIcon, statusColor, statusBgColor) = when(order.status) {
        OrderStatus.DELIVERED -> Tuple4("Delivered", Icons.Filled.CheckCircle, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.3f))
        OrderStatus.PENDING_SIGNATURE -> Tuple4("Pending Signature", Icons.Filled.Draw, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer.copy(alpha=0.3f))
        OrderStatus.PROCESSING -> Tuple4("Processing", Icons.Filled.HourglassEmpty, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surfaceVariant)
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("Order #${order.id}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(order.date, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                }
                Surface(color = statusBgColor, shape = RoundedCornerShape(50)) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(statusText, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), color = statusColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(order.itemsDesc, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(String.format("$%.2f", order.total), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onClick) {
                    Text("View Details", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

data class Tuple4<A,B,C,D>(val a: A, val b: B, val c: C, val d: D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(navController: NavController, orderId: String) {
    val order = sampleOrders.find { it.id == orderId } ?: sampleOrders.first()
    
    val (statusText, statusIcon, statusColor, statusBgColor) = when(order.status) {
        OrderStatus.DELIVERED -> Tuple4("Delivered", Icons.Filled.CheckCircle, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.3f))
        OrderStatus.PENDING_SIGNATURE -> Tuple4("Pending Signature", Icons.Filled.Draw, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer.copy(alpha=0.3f))
        OrderStatus.PROCESSING -> Tuple4("Processing", Icons.Filled.HourglassEmpty, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surfaceVariant)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column {
                            Text("Order #${order.id}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(order.date, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Surface(color = statusBgColor, shape = RoundedCornerShape(50)) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(statusText, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), color = statusColor)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Items", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(order.itemsDesc, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total Amount", style = MaterialTheme.typography.titleMedium)
                        Text(String.format("$%.2f", order.total), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Back to Orders", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: MediShopViewModel) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "Settings Unused", Toast.LENGTH_SHORT).show() }, modifier = Modifier.padding(end = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
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
                .verticalScroll(rememberScrollState())
                .semantics { testTagsAsResourceId = true }
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(112.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(56.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAtEKHQMMbyzYTArXCSYxaFYbdBmYzxa0Ehy_92xAwPxhTS9J8lzybYZRF_dpOKrum0Q3xw6thURmsvbE6j9gamxQEe619GBYp0KrRJ0d1NTytC9JcCLFxvwAZSGPW5s2uhMO0ncMNz6zwlbiZoWTviSbMXG-iUGP3GuGyVVxyhIlWFK2-rMYQXsbS6c8kdeH7pDYbJZsva6REBOI2rAs3LNu69Sf4KAjCT_1dWQQZ1-8AgeETIA0HW9LfqmdyBl36gYskXfMhLBthE",
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(56.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(MaterialTheme.colorScheme.surface.copy(alpha=0.8f)).padding(4.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = "Edit Photo", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(userProfile.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(50)).padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.testTag("email_id")
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(50)).padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.PhoneIphone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(userProfile.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("editProfile") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile Details")
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column {
                    ProfileMenuRow(icon = Icons.Filled.MedicalInformation, title = "My Prescriptions", onClick = { Toast.makeText(context, "My Prescriptions coming soon", Toast.LENGTH_SHORT).show() })
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileMenuRow(icon = Icons.Filled.LocalShipping, title = "Delivery Addresses", onClick = { navController.navigate("deliveryAddresses") })
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileMenuRow(icon = Icons.Filled.CreditCard, title = "Payment Methods", onClick = { Toast.makeText(context, "Manage Payment Methods", Toast.LENGTH_SHORT).show() })
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileMenuRow(icon = Icons.Filled.Notifications, title = "Notifications", onClick = { Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show() })
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileMenuRow(icon = Icons.Filled.Settings, title = "Settings", onClick = { navController.navigate("settings") })
                }
            }

            OutlinedButton(
                onClick = { 
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
            Spacer(modifier = Modifier.height(80.dp)) // Added space above bottom nav
        }
    }
}

@Composable
fun ProfileMenuRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 16.dp))
            Text("Manage your preferences and app configurations.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 24.dp))

            Text("PREFERENCES", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                Column {
                    SettingsRowToggle(icon = Icons.Filled.DarkMode, title = "Dark Mode", subtitle = "Adjust appearance for low-light environments", checked = false, onCheckedChange = {})
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsRowToggle(icon = Icons.Filled.Notifications, title = "Push Notifications", subtitle = "Receive alerts for order updates and reminders", checked = true, onCheckedChange = {})
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Language", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium))
                                Text("Select your preferred language", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("English (US)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                            Icon(Icons.Filled.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Text("ABOUT", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocalPharmacy, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MediShop", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text("Version 2.4.1 (Build 4092)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "MediShop provides secure, reliable access to your pharmacy needs. Our platform ensures rigorous data protection and compliance with clinical standards for your peace of mind.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(50), border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                        Icon(Icons.Filled.Description, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Terms of Service", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(50), border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                        Icon(Icons.Filled.Policy, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Privacy Policy", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRowToggle(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary, checkedTrackColor = MaterialTheme.colorScheme.primary))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(navController: NavController, viewModel: MediShopViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    
    var name by remember { androidx.compose.runtime.mutableStateOf(userProfile.name) }
    var email by remember { androidx.compose.runtime.mutableStateOf(userProfile.email) }
    var phone by remember { androidx.compose.runtime.mutableStateOf(userProfile.phone) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .semantics { testTagsAsResourceId = true }
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("edit_profile_name_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("email_id"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).testTag("edit_profile_phone_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            Button(
                onClick = { 
                    viewModel.updateProfile(name, email, phone)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag("edit_profile_save_button"),
                shape = RoundedCornerShape(50)
            ) {
                Text("Save Changes", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAddressesScreen(navController: NavController, viewModel: MediShopViewModel) {
    val addresses by viewModel.savedAddresses.collectAsState()
    
    // We'll show a simple UI for adding a new address for now to fulfill the requirement
    var showAddDialog by remember { androidx.compose.runtime.mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delivery Addresses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }, modifier = Modifier.padding(end = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Address")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        if (addresses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No delivery addresses saved", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { showAddDialog = true }, shape = RoundedCornerShape(50)) {
                        Text("Add New Address")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                items(addresses) { address ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(address.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(address.houseInfo, style = MaterialTheme.typography.bodyMedium)
                                Text("${address.city}, ${address.state} ${address.pincode}", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Phone: ${address.mobileNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { viewModel.removeAddress(address.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
        
        if (showAddDialog) {
            AddAddressDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { fullName, mobile, house, city, state, pin ->
                    viewModel.addAddress(com.example.Address(
                        fullName = fullName,
                        mobileNumber = mobile,
                        houseInfo = house,
                        city = city,
                        state = state,
                        pincode = pin
                    ))
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddAddressDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String, String, String) -> Unit) {
    var fullName by remember { androidx.compose.runtime.mutableStateOf("") }
    var mobile by remember { androidx.compose.runtime.mutableStateOf("") }
    var house by remember { androidx.compose.runtime.mutableStateOf("") }
    var city by remember { androidx.compose.runtime.mutableStateOf("") }
    var state by remember { androidx.compose.runtime.mutableStateOf("") }
    var pin by remember { androidx.compose.runtime.mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Delivery Address") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("address_name_input"), singleLine = true)
                OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("address_mobile_input"), singleLine = true)
                OutlinedTextField(value = house, onValueChange = { house = it }, label = { Text("Flat/House No, Building") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("address_house_input"))
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Town/City") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("address_city_input"), singleLine = true)
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.weight(1f).padding(end = 4.dp).testTag("address_state_input"), singleLine = true)
                    OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("Pincode") }, modifier = Modifier.weight(1f).padding(start = 4.dp).testTag("address_pincode_input"), singleLine = true)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(fullName, mobile, house, city, state, pin) },
                modifier = Modifier.testTag("address_save_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("address_cancel_button")
            ) {
                Text("Cancel")
            }
        }
    )
}

