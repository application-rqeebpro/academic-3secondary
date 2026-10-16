package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataRepository
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun SubjectsScreen(
    onSelectSubject: (Subject) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("subjects_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "المناهج الوزارية المقررة",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "اختر المادة للاطلاع على الوحدات والدروس والمسائل النموذجية",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(AppDataRepository.subjects) { subject ->
            val unitsCount = AppDataRepository.units.count { it.subjectId == subject.id }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectSubject(subject)
                        onNavigate(Screen.UNITS)
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(subject.colorHex).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = subject.iconEmoji, fontSize = 26.sp)
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = subject.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    color = Color(subject.colorHex).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = subject.track,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(subject.colorHex),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = subject.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$unitsCount وحدات دراسية مقررة",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun UnitsScreen(
    subject: Subject,
    onSelectLesson: (Lesson) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val subjectUnits = AppDataRepository.units.filter { it.subjectId == subject.id }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("units_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(subject.colorHex)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(subject.iconEmoji, fontSize = 34.sp)
                    Column {
                        Text(subject.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(subject.description, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
        }

        items(subjectUnits) { unit ->
            val unitLessons = AppDataRepository.lessons.filter { it.unitId == unit.id }
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(unit.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NavyPrimary)
                    Text(unit.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    if (unitLessons.isEmpty()) {
                        Text("الدروس قيد التجهيز من المشرف الأكاديمي", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        unitLessons.forEach { lesson ->
                            val isCompleted = AppDataRepository.completedLessonIds[lesson.id] == true
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        onSelectLesson(lesson)
                                        onNavigate(Screen.LESSON_DETAIL)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Icon(
                                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        tint = if (isCompleted) EmeraldSuccess else NavyPrimary
                                    )
                                    Column {
                                        Text(lesson.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(lesson.coreIdea, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonDetailScreen(
    lesson: Lesson
) {
    val context = LocalContext.current
    var showSimplerExplanation by remember { mutableStateOf(false) }
    var isLessonDone by remember { mutableStateOf(AppDataRepository.completedLessonIds[lesson.id] == true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("lesson_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("درس وزاري رقم ${lesson.lessonNumber}", fontSize = 12.sp, color = GoldLight, fontWeight = FontWeight.Bold)
                    Text(lesson.title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(lesson.coreIdea, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
                }
            }
        }

        // Simplified Explanation
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("📖 الشرح المفهومي المبسط:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                    Text(lesson.simplifiedExplanation, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // Formulas & Laws
        if (lesson.formulas.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyPrimary.copy(alpha = 0.08f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📐 القوانين والمعادلات المعتمدة:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                        Text(
                            text = lesson.formulas,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep,
                            lineHeight = 22.sp
                        )
                        if (lesson.symbolsExplanation.isNotBlank()) {
                            HorizontalDivider(color = NavyPrimary.copy(alpha = 0.2f))
                            Text(lesson.symbolsExplanation, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Solved Example Step-by-Step
        if (lesson.solvedExample.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("✍️ مسألة وزارية محلولة خطوة بخطوة:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmeraldSuccess)
                        Text(
                            text = lesson.solvedExample,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Practice Exercise
        if (lesson.practiceExercise.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("🎯 تدريب وزاري للمحاولة:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberWarning)
                        Text(lesson.practiceExercise, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Simpler Explanation Toggle
        if (lesson.easierExplanation.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💡 اشرح لي بأبسط من كذا (طريقة تفكير المعلم الخصوصي)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PurpleAccent)
                            TextButton(onClick = { showSimplerExplanation = !showSimplerExplanation }) {
                                Text(if (showSimplerExplanation) "إخفاء" else "عرض الشرح", color = PurpleAccent)
                            }
                        }
                        if (showSimplerExplanation) {
                            Text(lesson.easierExplanation, fontSize = 12.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // Completion Action
        item {
            Button(
                onClick = {
                    isLessonDone = !isLessonDone
                    AppDataRepository.completedLessonIds[lesson.id] = isLessonDone
                    Toast.makeText(
                        context,
                        if (isLessonDone) "تم تحديد الدرس كمكتمل ✔" else "تم إلغاء التحديد",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLessonDone) EmeraldSuccess else NavyPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isLessonDone) "الدرس مكتمل بنجاح ✔" else "تحديد الدرس كمكتمل ومفهوم", fontWeight = FontWeight.Bold)
            }
        }
    }
}
