package com.example.myminiproject.ui.screens

import android.Manifest
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.VoiceInputViewModel
import com.example.myminiproject.ui.viewmodels.VoiceState
import java.util.Locale

@Composable
fun VoiceInputScreen(
    navController: NavController,
    viewModel: VoiceInputViewModel = viewModel()
) {
    val context = LocalContext.current
    val voiceState by viewModel.voiceState.collectAsState()
    val parsedTransaction by viewModel.parsedTransaction.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    
    var transcribedText by remember { mutableStateOf("") }
    var hasPermission by remember { mutableStateOf(false) }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    
    // Speech recognizer launcher
    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (!matches.isNullOrEmpty()) {
            transcribedText = matches[0]
            // Parse the transcribed text
            viewModel.parseVoiceInput(transcribedText)
        }
    }
    
    // Request permission on first composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    
    // Handle success - navigate back
    LaunchedEffect(voiceState) {
        if (voiceState is VoiceState.TransactionSaved) {
            kotlinx.coroutines.delay(2000)
            navController.popBackStack()
        }
    }
    
    fun startVoiceRecognition() {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            return
        }
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, when(selectedLanguage) {
                "Hindi" -> Locale("hi", "IN")
                "Marathi" -> Locale("mr", "IN")
                else -> Locale.ENGLISH
            })
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something like: Aaj 500 rupaye ki sabzi becha")
        }
        speechLauncher.launch(intent)
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
                        colors = listOf(Purple600, Color(0xFF4F46E5)),
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
                Column {
                    Text(stringResource(R.string.voice_input), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(stringResource(R.string.speak_to_add), fontSize = 13.sp, color = Purple200)
                }
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.select_language), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("English", "Hindi", "Marathi").forEach { lang ->
                            FilterChip(
                                selected = selectedLanguage == lang,
                                onClick = { viewModel.setLanguage(lang) },
                                label = { Text(lang, fontSize = 13.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Purple600,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Microphone Button
            val scale by animateFloatAsState(
                targetValue = if (voiceState is VoiceState.Recording) 1.1f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "mic_scale"
            )
            
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Purple600, Color(0xFF4F46E5))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { startVoiceRecognition() },
                    modifier = Modifier.size(120.dp),
                    enabled = voiceState !is VoiceState.Loading
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Record",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Tap to speak",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Purple600
            )
            
            Text(
                "Example: \"Aaj 500 rupaye ki sabzi becha\"",
                fontSize = 12.sp,
                color = Gray500,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Transcribed Text
            if (transcribedText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Blue50)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Blue600, modifier = Modifier.size(20.dp))
                            Text("You said:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Blue600)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(transcribedText, fontSize = 14.sp, color = Gray800)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Parsed Transaction
            if (parsedTransaction != null && voiceState !is VoiceState.Loading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Detected Transaction", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (parsedTransaction!!.confidence == "high") Green100 else Amber100
                            ) {
                                Text(
                                    parsedTransaction!!.confidence.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (parsedTransaction!!.confidence == "high") Green600 else Amber600,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Amount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Amount:", fontSize = 13.sp, color = Gray600)
                            Text(
                                "₹${String.format("%,.0f", parsedTransaction!!.amount)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (parsedTransaction!!.type == "income") Green600 else Red500
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Gray100)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Type
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Type:", fontSize = 13.sp, color = Gray600)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (parsedTransaction!!.type == "income") Green100 else Red100
                            ) {
                                Text(
                                    parsedTransaction!!.type.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (parsedTransaction!!.type == "income") Green600 else Red500,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Category
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Category:", fontSize = 13.sp, color = Gray600)
                            Text(parsedTransaction!!.category, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Gray800)
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Description
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Description:", fontSize = 13.sp, color = Gray600)
                            Text(
                                parsedTransaction!!.description,
                                fontSize = 13.sp,
                                color = Gray800,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            transcribedText = ""
                            viewModel.clearParsedTransaction()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Try Again", fontWeight = FontWeight.Medium)
                    }
                    
                    Button(
                        onClick = { viewModel.saveTransaction() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple600),
                        enabled = voiceState !is VoiceState.Loading
                    ) {
                        if (voiceState is VoiceState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Save Transaction", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            
            // Loading State
            if (voiceState is VoiceState.Loading) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(color = Purple600)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Processing...", fontSize = 13.sp, color = Gray600)
            }
            
            // Error State
            if (voiceState is VoiceState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Red100)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Red500, modifier = Modifier.size(20.dp))
                        Text(
                            (voiceState as VoiceState.Error).message,
                            fontSize = 13.sp,
                            color = Red600
                        )
                    }
                }
            }
            
            // Success State
            if (voiceState is VoiceState.TransactionSaved) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green100)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Green600, modifier = Modifier.size(20.dp))
                        Text(
                            "Transaction saved successfully!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Green600
                        )
                    }
                }
            }
        }
    }
}
