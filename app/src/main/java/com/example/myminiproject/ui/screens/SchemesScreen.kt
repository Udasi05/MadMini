package com.example.myminiproject.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.data.api.dto.SchemeEligibility
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.SchemesUiState
import com.example.myminiproject.ui.viewmodels.SchemesViewModel

@Composable
fun SchemesScreen(
    navController: NavController,
    viewModel: SchemesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var search by remember { mutableStateOf("") }
    var activeCategory by remember { mutableStateOf("All") }
    var selectedScheme by remember { mutableStateOf<SchemeEligibility?>(null) }

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
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
        ) {
            Text("Government Schemes", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Welfare schemes for farmers & workers", fontSize = 13.sp, color = Green200)

            Spacer(modifier = Modifier.height(12.dp))

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

        when (val state = uiState) {
            is SchemesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Green600)
                }
            }
            is SchemesUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message, color = Red400, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchSchemes() }) {
                        Text("Retry")
                    }
                }
            }
            is SchemesUiState.Success -> {
                // Summary AI Box
                if (state.summary.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Green100),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Green600, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(state.summary, fontSize = 13.sp, color = GreenDark, lineHeight = 18.sp)
                        }
                    }
                }

                val filtered = state.schemes.filter { s ->
                    s.schemeName.lowercase().contains(search.lowercase())
                }

                // Schemes List
                if (filtered.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(40.dp), tint = Gray400)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No schemes found", fontSize = 14.sp, color = Gray400)
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        filtered.forEach { scheme ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedScheme = scheme },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(Green100),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Description,
                                            contentDescription = null,
                                            tint = Green600,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(scheme.schemeName, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Yield Benefit: ${scheme.benefits}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Green600,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Gray400, modifier = Modifier.size(16.dp))
                                }
                            }
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
private fun SchemeDetailSheet(scheme: SchemeEligibility, onDismiss: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(scheme.schemeName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Background & Eligibility status
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if(scheme.eligible) Green100 else Red100)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        if(scheme.eligible) Icons.Default.CheckCircle else Icons.Default.Cancel, 
                        contentDescription = null, 
                        tint = if(scheme.eligible) Green600 else Red400 
                    )
                    Column {
                        Text("Justification", fontSize = 11.sp, color = Gray500)
                        Text(scheme.justification, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray800)
                    }
                }

                // Benefits
                Column {
                    Text("Benefits", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(scheme.benefits, fontSize = 13.sp, color = Gray600, lineHeight = 22.sp)
                }

                // Documents
                if(scheme.documentsRequired.isNotEmpty()) {
                    Column {
                        Text("Documents Required", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                        Spacer(modifier = Modifier.height(8.dp))
                        scheme.documentsRequired.forEach { doc ->
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
                }
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
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
                
                if (scheme.applicationLink.isNotBlank()) {
                    Button(
                        onClick = { 
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(scheme.applicationLink))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green600)
                    ) {
                        Text("Apply Now", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
