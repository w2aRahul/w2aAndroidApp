package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.MediShopViewModel
import com.example.Product
import com.example.sampleProducts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MediShopViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = "Good Morning,",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Rahul Arora",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Profile */ }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("productList") }) {
                        Icon(Icons.Filled.NotificationsNone, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .semantics { testTagsAsResourceId = true }
        ) {
            // Search Bar fake
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("home_search_bar")
                    .clickable { navController.navigate("productList") },
                border = null
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "Search medicines, symptoms...",
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Categories", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                TextButton(onClick = { navController.navigate("productList") }, contentPadding = PaddingValues(0.dp)) {
                    Text("See All", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CategoryItem(icon = Icons.Filled.Thermostat, title = "Fever", containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.primary, onClick = { navController.navigate("productList?category=Fever") })
                CategoryItem(icon = Icons.Filled.Medication, title = "Pharmacy", containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.primary, onClick = { navController.navigate("productList?category=Pharmacy") })
                CategoryItem(icon = Icons.Filled.Bloodtype, title = "Diabetes", containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.primary, onClick = { navController.navigate("productList?category=Diabetes") })
                CategoryItem(icon = Icons.Filled.Favorite, title = "Heart", containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.primary, onClick = { navController.navigate("productList?category=Heart") })
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // "Today's Focus" Banner (using HTML's primaryContainer rounded 28px)
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Today's Focus", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Hydration limits", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate("hydration") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("home_hydration_review_button")
                        ) {
                            Text("Review")
                        }
                    }
                    Icon(Icons.Filled.WaterDrop, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Popular Medicines", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                TextButton(onClick = { navController.navigate("productList") }, contentPadding = PaddingValues(0.dp)) {
                    Text("See All", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                items(sampleProducts.size) { index ->
                    val product = sampleProducts[index]
                    ProductCardLinear(product = product, onClick = { navController.navigate("product/${product.id}") }, onAddClick = { viewModel.addToCart(product, 1) })
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable { onClick() }
    ) {
        Surface(
            color = containerColor,
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shadowElevation = 1.dp,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ProductCardLinear(product: Product, onClick: () -> Unit, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = product.imageResUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Inside
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = product.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "$${product.price}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductListScreen(navController: NavController, viewModel: MediShopViewModel, initialCategory: String = "") {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(if (initialCategory.isNotEmpty()) initialCategory else "All") }
    
    val categories = listOf("All", "Fever", "Pharmacy", "Diabetes", "Heart", "Pain Relief", "Allergy", "Digestion", "Antibiotic", "Supplement", "Cardiology")
    
    val filteredProducts = sampleProducts.filter { product ->
        val matchesCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) || product.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Medicines", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Filled.Tune, contentDescription = "Filter", tint = MaterialTheme.colorScheme.onSurface)
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
                .semantics { testTagsAsResourceId = true }
        ) {
            // Search and Filters
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search medicines, formulas...", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier.weight(1f).padding(start = 4.dp).testTag("email_id"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    }
                }
                
                LazyRow(
                    modifier = Modifier.padding(top = 16.dp).testTag("list_id"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            onClick = { selectedCategory = category }, 
                            label = { Text(category) }, 
                            selected = selectedCategory == category,
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        ) 
                    }
                }
            }
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCardGrid(product, onClick = { navController.navigate("product/${product.id}") }, onAddClick = { viewModel.addToCart(product, 1) })
                }
            }
        }
    }
}

@Composable
fun ProductCardGrid(product: Product, onClick: () -> Unit, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = product.imageResUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Inside
                )
                if (product.isBestSeller) {
                    Text(
                        text = "BEST SELLER",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(20.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                } else if (product.isGeneric) {
                    Text(
                        text = "GENERIC",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = product.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (product.oldPrice != null) {
                            Text(text = "$${product.oldPrice}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, textDecoration = TextDecoration.LineThrough)
                        } else {
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                        Text(text = "$${product.price}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(navController: NavController) {
    var waterIntake by remember { mutableStateOf(2) }
    var dailyGoal by remember { mutableStateOf(8) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hydration Limits") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                    ) {
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Circular progress or icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
            ) {
                CircularProgressIndicator(
                    progress = { waterIntake.toFloat() / dailyGoal },
                    modifier = Modifier.size(160.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    strokeWidth = 12.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.WaterDrop, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$waterIntake / $dailyGoal", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    Text("Glasses", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                "Daily Hydration Goal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Drinking enough water every day represents an important step in maintaining your health and well-being. Adjust your daily goal based on your needs.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { if (waterIntake > 0) waterIntake-- },
                    modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                Button(
                    onClick = { if (waterIntake < dailyGoal) waterIntake++ },
                    modifier = Modifier.height(56.dp).weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Water")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Glass")
                }
            }
        }
    }
}
