package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.MediShopViewModel
import com.example.sampleProducts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductDetailScreen(navController: NavController, viewModel: MediShopViewModel, productId: String) {
    val product = sampleProducts.find { it.id == productId } ?: return
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { }, modifier = Modifier.padding(end = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))) {
                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "Save", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quantity", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Filled.Remove, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(text = "$quantity", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface)
                            IconButton(onClick = { if (quantity < 10) quantity++ }, modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))) {
                                Icon(Icons.Filled.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.addToCart(product, quantity)
                                navController.navigate("cart")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier.weight(1f).height(56.dp).testTag("product_detail_add_to_cart_button")
                        ) {
                            Text("Add to Cart", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding())
                .semantics { testTagsAsResourceId = true }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = product.imageResUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(48.dp),
                    contentScale = ContentScale.Fit
                )
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Rx Required", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium))
                    }
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.padding(bottom = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50)) {
                        Text(text = product.category, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50)) {
                        Text(text = product.formFactor.takeIf { it.isNotBlank() } ?: "Various", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Text(text = product.name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = "$${product.price}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    Text(text = " / ${product.formFactor}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Medication Details", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = product.details.takeIf { it.isNotBlank() } ?: product.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        
                        if (product.dosage.isNotBlank()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Filled.MedicalServices, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Standard Dosage", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = product.dosage, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                if (product.safetyInfo.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Safety Information", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.error)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            product.safetyInfo.forEach { info ->
                                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                    Text("•", modifier = Modifier.padding(end = 8.dp), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
                                    Text(text = info, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
