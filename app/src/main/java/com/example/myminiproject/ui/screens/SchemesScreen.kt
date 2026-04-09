package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.data.model.Scheme
import com.example.myminiproject.ui.theme.*

@Composable
fun SchemesScreen(navController: NavController) {
    val schemes = remember { Scheme.getAllSchemes() }
    var search by remember { mutableStateOf("") }
    var activeCategory by remember { mutableStateOf("All") }
    var selectedScheme by remember { mutableStateOf<Scheme?>(null) }

    val filtered = schemes.filter { s ->
        val matchCat = activeCategory == "All" || s.category == activeCategory
        val matchSearch = s.name.lowercase().contains(search.lowercase()) ||
                s.fullName.lowercase().contains(search.lowercase())
        matchCat && matchSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(GreenDark, Green600),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 20.dp)
        ) {
            Text("Government Schemes", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Welfare schemes for farmers & workers", fontSize = 13.sp, color = Green200)

            Spacer(modifier = Modifier.height(16.dp))

            // Search
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search schemes...", color = Color.White.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.3f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.15f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }

        // Category Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Scheme.categories.forEach { cat ->
                val isActive = activeCategory == cat
                FilterChip(
                    selected = isActive,
                    onClick = { activeCategory = cat },
                    label = { Text(cat, fontSize = 13.sp, fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Green600,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Gray500
                    ),
                    border = if (isActive) null else FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = Gray200)
                )
            }
        }

        // Schemes List
        if (filtered.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🔍", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("No schemes found", fontSize = 14.sp, color = Gray400)
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filtered.forEach { scheme ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedScheme = scheme },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(Gray50),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(scheme.emoji, fontSize = 24.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(scheme.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
                                    Text(
                                        scheme.tag,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(scheme.tagColorHex))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(scheme.fullName, fontSize = 12.sp, color = Gray400, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Benefit: ${scheme.benefit}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(scheme.benefitColorHex),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(scheme.benefitColorHex).copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Gray400, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Scheme Detail Bottom Sheet
    if (selectedScheme != null) {
        SchemeDetailSheet(scheme = selectedScheme!!, onDismiss = { selectedScheme = null })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SchemeDetailSheet(scheme: Scheme, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(GreenDark, Green600))
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(scheme.emoji, fontSize = 28.sp)
                    Column {
                        Text(scheme.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(scheme.fullName, fontSize = 11.sp, color = Green200)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Benefit badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(scheme.benefitColorHex).copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🎁", fontSize = 20.sp)
                    Column {
                        Text("Benefit", fontSize = 11.sp, color = Gray500)
                        Text(scheme.benefit, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(scheme.benefitColorHex))
                    }
                }

                // About
                Column {
                    Text("About this Scheme", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(scheme.description, fontSize = 13.sp, color = Gray600, lineHeight = 22.sp)
                }

                // Eligibility
                Column {
                    Text("Eligibility", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(scheme.eligibility, fontSize = 13.sp, color = Gray600)
                }

                // Documents
                Column {
                    Text("Documents Required", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(8.dp))
                    scheme.documents.forEach { doc ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Green500))
                            Text(doc, fontSize = 13.sp, color = Gray600)
                        }
                    }
                }

                // How to Apply
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("📍 How to Apply", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Green600)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(scheme.howToApply, fontSize = 13.sp, color = Color(0xFF15803D), lineHeight = 20.sp)
                    }
                }
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray100)
                ) {
                    Text("Close", color = Gray500, fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = { /* Open link */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600)
                ) {
                    Text("Apply Now", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
