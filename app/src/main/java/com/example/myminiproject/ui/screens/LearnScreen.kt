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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.data.model.Lesson
import com.example.myminiproject.ui.theme.*

@Composable
fun LearnScreen(navController: NavController) {
    val lessons = remember { Lesson.getAllLessons() }
    var completedLessons by remember { mutableStateOf(listOf<Int>()) }
    var openLesson by remember { mutableStateOf<Lesson?>(null) }

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
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
            ) {
                Text(stringResource(R.string.learn_finance), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(R.string.simple_lessons), fontSize = 13.sp, color = Blue200)

                Spacer(modifier = Modifier.height(16.dp))

                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.your_progress), fontSize = 12.sp, color = Blue200)
                    Text(
                        "${completedLessons.size} of ${lessons.size} ${stringResource(R.string.completed)}",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Lessons
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                lessons.forEachIndexed { index, lesson ->
                    val done = lesson.id in completedLessons
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openLesson = lesson },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = if (done) androidx.compose.foundation.BorderStroke(1.5.dp, Green500)
                        else androidx.compose.foundation.BorderStroke(1.dp, Color(lesson.borderColorHex).copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(lesson.colorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    lesson.emoji,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(lesson.borderColorHex)
                                )
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
                                        "Completed",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Ask Finance Assistant button
            Button(
                onClick = { navController.navigate(com.example.myminiproject.navigation.Screen.ChatBot.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.ask_finance_assistant), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
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
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp),
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
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White, modifier = Modifier.size(20.dp))
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
                .padding(16.dp)
        ) {
            // Lesson icon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(lesson.colorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    lesson.emoji,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(lesson.borderColorHex)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Green500 else Blue600
                )
            ) {
                Text(
                    if (isCompleted) stringResource(R.string.completed_go_back) else stringResource(R.string.mark_as_complete),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
