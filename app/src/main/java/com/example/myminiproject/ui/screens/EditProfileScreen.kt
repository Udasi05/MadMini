package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.ProfileState
import com.example.myminiproject.ui.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val user = FirebaseAuth.getInstance().currentUser
    
    var name by remember { mutableStateOf(user?.displayName ?: "") }
    var state by remember { mutableStateOf("") }
    var landHolding by remember { mutableStateOf("") }
    var crops by remember { mutableStateOf("") }
    
    val profileState by viewModel.profileState.collectAsState()
    val profileData by viewModel.profileData.collectAsState()
    
    // Load profile data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    // Update fields when profile data loads
    LaunchedEffect(profileData) {
        profileData?.let { data ->
            if (name.isEmpty()) name = data.name
            if (state.isEmpty()) state = data.state
            if (landHolding.isEmpty()) landHolding = if (data.landHolding > 0) data.landHolding.toString() else ""
            if (crops.isEmpty()) crops = data.crops.joinToString(", ")
        }
    }
    
    // Handle success state
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            kotlinx.coroutines.delay(2000)
            navController.popBackStack()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Blue900, Blue600),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Text(stringResource(R.string.edit_profile), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        
        // Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.full_name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            // State Field
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text(stringResource(R.string.state)) },
                placeholder = { Text(stringResource(R.string.state_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            // Land Holding Field
            OutlinedTextField(
                value = landHolding,
                onValueChange = { landHolding = it },
                label = { Text(stringResource(R.string.land_holding)) },
                placeholder = { Text(stringResource(R.string.land_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            // Crops Field
            OutlinedTextField(
                value = crops,
                onValueChange = { crops = it },
                label = { Text(stringResource(R.string.crops_grown)) },
                placeholder = { Text(stringResource(R.string.crops_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Save Button
            Button(
                onClick = {
                    viewModel.updateProfile(name, state, landHolding, crops)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                enabled = profileState !is ProfileState.Loading && name.isNotBlank()
            ) {
                if (profileState is ProfileState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.save_changes), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
            
            // Error Message
            if (profileState is ProfileState.Error) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Red100)
                ) {
                    Text(
                        text = (profileState as ProfileState.Error).message,
                        color = Red500,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            
            // Success Message
            if (profileState is ProfileState.Success) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green100)
                ) {
                    Text(
                        text = "✓ ${(profileState as ProfileState.Success).message}",
                        color = Green600,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
