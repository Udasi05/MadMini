package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.data.model.Lesson
import com.example.myminiproject.ui.theme.*

@Composable
fun LearnScreen(navController: NavController) {
    val lessons = remember { Lesson.getAllLessons() }
    var completedLessons by remember { mutableStateOf(listOf<Int>()) }
    var openLesson by remember { mutableStateOf<Lesson?>(null) }
    var showChat by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            colors = listOf(Blue900, Blue600),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 20.dp)
            ) {
                Text("Learn Finance", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Simple lessons for better money decisions", fontSize = 13.sp, color = Blue200)

                Spacer(modifier = Modifier.height(16.dp))

                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Your Progress", fontSize = 12.sp, color = Blue200)
                    Text(
                        "${completedLessons.size} of ${lessons.size} completed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { completedLessons.size.toFloat() / lessons.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Green400,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lessons
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                lessons.forEachIndexed { index, lesson ->
                    val done = lesson.id in completedLessons
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openLesson = lesson },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = if (done) androidx.compose.foundation.BorderStroke(1.5.dp, Green500)
                        else androidx.compose.foundation.BorderStroke(1.dp, Color(lesson.borderColorHex).copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(lesson.colorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(lesson.emoji, fontSize = 24.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    lesson.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(lesson.borderColorHex)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(lesson.duration, fontSize = 12.sp, color = Gray400)
                                if (done) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "✓ Completed",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Green600,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Green100)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Gray400, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ask Finance Assistant button
            Button(
                onClick = { showChat = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ask Finance Assistant", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Lesson Detail
        if (openLesson != null) {
            LessonDetailOverlay(
                lesson = openLesson!!,
                isCompleted = openLesson!!.id in completedLessons,
                onComplete = {
                    if (openLesson!!.id !in completedLessons) {
                        completedLessons = completedLessons + openLesson!!.id
                    }
                    openLesson = null
                },
                onDismiss = { openLesson = null }
            )
        }

        // Chat modal
        if (showChat) {
            LearnChatModal(onDismiss = { showChat = false })
        }
    }
}

@Composable
private fun LessonDetailOverlay(
    lesson: Lesson,
    isCompleted: Boolean,
    onComplete: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Blue900, Blue600)
                    )
                )
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(lesson.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(lesson.duration, fontSize = 12.sp, color = Blue200)
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Emoji icon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(lesson.colorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(lesson.emoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            lesson.content.split("\n\n").forEach { paragraph ->
                paragraph.split("\n").forEach { line ->
                    val isBullet = line.startsWith("•") || line.startsWith("1.") || line.startsWith("2.") ||
                            line.startsWith("3.") || line.startsWith("4.")
                    Text(
                        text = line,
                        fontSize = 15.sp,
                        color = Gray700,
                        lineHeight = 24.sp,
                        fontWeight = if (line.all { it.isUpperCase() || it == ' ' }) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(start = if (isBullet) 8.dp else 0.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Bottom button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            HorizontalDivider(color = Gray100)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Green500 else Blue600
                )
            ) {
                Text(
                    if (isCompleted) "✓ Completed! Go Back" else "Mark as Complete ✓",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearnChatModal(onDismiss: () -> Unit) {
    val chatResponses = mapOf(
        "saving" to "Saving regularly is the foundation of financial security. Even ₹50 per day saved adds up to ₹18,000 per year! Start a recurring deposit at your bank.",
        "loan" to "Government loans like Kisan Credit Card (KCC) and Mudra Loan offer interest rates of 4-12%, much cheaper than private moneylenders. Visit your nearest bank to apply.",
        "insurance" to "PMJJBY (life insurance) costs only ₹330/year for ₹2 lakh cover, and PMSBY (accident) costs ₹12/year. Both are available at any bank.",
        "scheme" to "Popular government schemes: PM-KISAN (₹6000/year for farmers), PMAY (housing), Ujjwala Yojana (LPG), Jan Dhan Yojana (free bank account).",
        "interest" to "Interest is extra money paid on loans or earned on deposits. Government banks offer 6-9% on savings and 7-12% on loans."
    )

    data class ChatMsg(val from: String, val text: String)

    var messages by remember {
        mutableStateOf(
            listOf(ChatMsg("bot", "👋 Hi! I'm your Finance Assistant. Ask me anything about saving, loans, insurance, or government schemes!"))
        )
    }
    var input by remember { mutableStateOf("") }

    fun getResponse(msg: String): String {
        val lower = msg.lowercase()
        return when {
            lower.contains("save") || lower.contains("saving") -> chatResponses["saving"]!!
            lower.contains("loan") || lower.contains("borrow") -> chatResponses["loan"]!!
            lower.contains("insurance") -> chatResponses["insurance"]!!
            lower.contains("scheme") || lower.contains("yojana") -> chatResponses["scheme"]!!
            lower.contains("interest") || lower.contains("rate") -> chatResponses["interest"]!!
            else -> "Great question! I'm here to help you learn about personal finance. Ask about saving, loans, insurance, or government schemes."
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.7f)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(Blue600, Blue700))
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("Finance Assistant", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        Text("Ask about savings, loans, schemes", fontSize = 11.sp, color = Blue200)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            // Messages
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                messages.forEach { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.from == "user") Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (msg.from == "user") Blue600 else Gray100
                            ),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                msg.text,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = if (msg.from == "user") Color.White else Gray800,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }

            // Quick prompts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Saving tips", "Best loan", "Insurance", "Schemes").forEach { q ->
                    AssistChip(
                        onClick = { input = q },
                        label = { Text(q, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = AssistChipDefaults.assistChipColors(containerColor = Blue50, labelColor = Blue600),
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = Blue200)
                    )
                }
            }

            // Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Ask me anything...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue600,
                        unfocusedBorderColor = Gray200,
                        focusedContainerColor = Gray50,
                        unfocusedContainerColor = Gray50
                    )
                )
                IconButton(
                    onClick = {
                        if (input.isNotBlank()) {
                            val userMsg = input.trim()
                            messages = messages + ChatMsg("user", userMsg)
                            input = ""
                            messages = messages + ChatMsg("bot", getResponse(userMsg))
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (input.isNotBlank()) Blue600 else Gray200)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (input.isNotBlank()) Color.White else Gray400,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
