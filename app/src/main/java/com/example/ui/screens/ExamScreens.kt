package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataRepository
import com.example.model.Exam
import com.example.model.ExamResult
import com.example.model.Question
import com.example.model.Screen
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ExamsScreen(
    onStartExam: (Exam) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("exams_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("نماذج الاختبارات الوزارية المؤتمتة", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("تدرب على أسئلة الامتحانات الرسمية مع توقيت زمني دقيق وتقييم مباشر للدرجات.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        items(AppDataRepository.exams) { exam ->
            val subject = AppDataRepository.subjects.find { it.id == exam.subjectId }
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = NavyPrimary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = subject?.name ?: "مقرر وزاري",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                            Text("${exam.durationMinutes} دقيقة", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                        }
                    }

                    Text(exam.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("القسم: ${exam.track} | عدد الأسئلة: ${exam.totalQuestions} سؤالاً وزارياً", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Button(
                        onClick = { onStartExam(exam) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("بدء الاختبار الآن", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ExamSessionScreen(
    exam: Exam,
    onSubmitExam: (ExamResult) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    var secondsLeft by remember { mutableStateOf(exam.durationMinutes * 60) }
    var showConfirmSubmit by remember { mutableStateOf(false) }

    // Grab questions
    val examQuestions = remember(exam) {
        val qList = AppDataRepository.questions.filter { exam.questionIds.contains(it.id) || it.subjectId == exam.subjectId }
        if (qList.isNotEmpty()) qList else AppDataRepository.questions.take(5)
    }

    // Countdown Timer
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
    }

    val currentQ = examQuestions.getOrNull(currentIndex) ?: examQuestions.first()

    fun finishTest() {
        var correct = 0
        examQuestions.forEach { q ->
            val ans = userAnswers[q.id]
            if (ans != null && ans.equals(q.correctOption, ignoreCase = true)) {
                correct++
            }
        }
        val score = if (examQuestions.isNotEmpty()) (correct * 100) / examQuestions.size else 0
        val result = ExamResult(
            id = System.currentTimeMillis(),
            userId = AppDataRepository.currentUser.value?.id ?: 2L,
            examId = exam.id,
            subjectId = exam.subjectId,
            score = score,
            totalQuestions = examQuestions.size,
            correctAnswers = correct,
            wrongAnswers = examQuestions.size - correct,
            isPassed = score >= 50,
            answers = userAnswers.toMap()
        )
        onSubmitExam(result)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("exam_session_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Timer & Question Progress Bar
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السؤال ${currentIndex + 1} من ${examQuestions.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )

                    val minutes = secondsLeft / 60
                    val secs = secondsLeft % 60
                    Surface(
                        color = if (secondsLeft < 180) CrimsonSecurity.copy(alpha = 0.15f) else NavyPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = String.format("⏱ %02d:%02d", minutes, secs),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (secondsLeft < 180) CrimsonSecurity else NavyPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / examQuestions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NavyPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        // Question Body
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = currentQ.questionText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 24.sp
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                if (currentQ.type == "TRUE_FALSE") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("TRUE" to "صواب (✔)", "FALSE" to "خطأ (✖)").forEach { (key, label) ->
                            val isSelected = userAnswers[currentQ.id] == key
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { userAnswers[currentQ.id] = key },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                } else {
                    listOf(
                        "A" to currentQ.optionA,
                        "B" to currentQ.optionB,
                        "C" to currentQ.optionC,
                        "D" to currentQ.optionD
                    ).filter { it.second.isNotBlank() }.forEach { (optKey, optText) ->
                        val isSelected = userAnswers[currentQ.id] == optKey
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { userAnswers[currentQ.id] = optKey },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White else NavyPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optKey,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) NavyPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = optText,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation bottom row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { if (currentIndex > 0) currentIndex-- },
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("السؤال السابق")
            }

            if (currentIndex < examQuestions.size - 1) {
                Button(
                    onClick = { currentIndex++ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("السؤال التالي")
                }
            } else {
                Button(
                    onClick = { showConfirmSubmit = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("تسليم الاختبار", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showConfirmSubmit) {
            AlertDialog(
                onDismissRequest = { showConfirmSubmit = false },
                title = { Text("تأكيد إنهاء الاختبار الوزاري") },
                text = { Text("هل أنت متأكد من تسليم الإجابات وإنهاء الاختبار ورصد النتيجة فورياً؟") },
                confirmButton = {
                    Button(onClick = {
                        showConfirmSubmit = false
                        finishTest()
                    }) {
                        Text("نعم، تسليم الاختبار")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmSubmit = false }) {
                        Text("الاستمرار في الحل")
                    }
                }
            )
        }
    }
}

@Composable
fun ExamResultScreen(
    result: ExamResult,
    onNavigate: (Screen) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("exam_result_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (result.isPassed) EmeraldSuccess else CrimsonSecurity
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (result.isPassed) Icons.Default.EmojiEvents else Icons.Default.Warning,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = if (result.isPassed) "مبارك! اجتزت الاختبار بنجاح" else "فرصة أخرى! ثابر للمزيد من التفوق",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "الدرجة النهائية: %${result.score}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "الإجابات الصحيحة: ${result.correctAnswers} من إجمالي ${result.totalQuestions} سؤالاً",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            Button(
                onClick = { onNavigate(Screen.EXAMS) },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("العودة لقائمة الامتحانات", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QuestionBankScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf("الكل") }
    val revealedAnswerIds = remember { mutableStateMapOf<Int, Boolean>() }

    val subjects = listOf("الكل", "الرياضيات", "الفيزياء", "الكيمياء")

    val filteredQuestions = AppDataRepository.questions.filter { q ->
        val subj = AppDataRepository.subjects.find { it.id == q.subjectId }
        val matchesSubj = selectedSubjectFilter == "الكل" || subj?.name == selectedSubjectFilter
        val matchesSearch = searchQuery.isBlank() || q.questionText.contains(searchQuery.trim())
        matchesSubj && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("question_bank_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("بنك الأسئلة الوزارية الشاملة", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("استعرض مئات الأسئلة المؤتمتة مع إمكانية إظهار الإجابة والتعليل فورياً.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("بحث في الأسئلة الوزارية...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(subjects) { s ->
                    FilterChip(
                        selected = selectedSubjectFilter == s,
                        onClick = { selectedSubjectFilter = s },
                        label = { Text(s) }
                    )
                }
            }
        }

        items(filteredQuestions) { q ->
            val isRevealed = revealedAnswerIds[q.id] == true
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(q.questionText, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (q.type == "TRUE_FALSE") "نوع السؤال: صح/خطأ" else "نوع السؤال: اختيار من متعدد",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(onClick = { revealedAnswerIds[q.id] = !isRevealed }) {
                            Text(if (isRevealed) "إخفاء الإجابة" else "كشف الإجابة والتعليل", color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isRevealed) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("الخيار المعتمد: ${q.correctOption}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldSuccess)
                                if (q.explanation.isNotBlank()) {
                                    Text("التعليل الوزاري: ${q.explanation}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
