package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationalSolution
import com.example.model.QuestionSolvedItem
import com.example.solver.EducationalSolver
import com.example.ui.theme.*

@Composable
fun SolveMyQuestionScreen() {
    val context = LocalContext.current
    var questionInput by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("الرياضيات") }
    var isExamSheetRequested by remember { mutableStateOf(false) }
    var isSolving by remember { mutableStateOf(false) }
    var solutionResult by remember { mutableStateOf<EducationalSolution?>(null) }
    var expandedItemNumbers by remember { mutableStateOf(setOf<Int>()) }

    val subjects = listOf("الرياضيات", "الفيزياء", "الكيمياء", "الأحياء", "اللغة الإنجليزية", "القرآن والإسلامية")

    val sampleQuestions = listOf(
        "احسب مقياس وسعة العدد ع = 1 + ت√3 باستخدام ديموافر",
        "حل ورقة امتحان وزاري مؤتمت كاملة (50 سؤالاً نموذجياً)",
        "قذفت كرة بسرعة 40 م/ث وبزاوية 30°، احسب أقصى ارتفاع والمدى",
        "احسب ثابت الاتزان Kc للتفاعل N2 + 3H2 ⇌ 2NH3",
        "تزاوج نباتي بازلاء طويل هجين Tt x Tt ما نسبة الجيل الأول؟"
    )

    fun executeSolve() {
        if (questionInput.isBlank() && !isExamSheetRequested) {
            Toast.makeText(context, "يرجى كتابة السؤال أو اختيار نموذج للاختبار", Toast.LENGTH_SHORT).show()
            return
        }
        isSolving = true
        // High quality curriculum pedagogical solver
        solutionResult = EducationalSolver.solve(
            input = questionInput,
            subject = selectedSubject,
            isExamSheetRequest = isExamSheetRequested
        )
        isSolving = false
        Toast.makeText(context, "تم استخراج الحل الوزاري المفصل بنجاح!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("solve_my_question_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldLight)
                        }
                        Column {
                            Text(
                                text = "مُحرك حل المسائل والأسئلة الوزارية",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "حل مفصل بالمعطيات والقوانين والتعويض وحل أوراق الامتحانات حتى 50 سؤالاً",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // Subject selector chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "اختر المادة الدراسية:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(subjects) { subj ->
                        val isSelected = subj == selectedSubject
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSubject = subj },
                            label = { Text(subj, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Question Input Box
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = { questionInput = it },
                        label = { Text("اكتب أو الصق نص السؤال أو المسألة هنا:") },
                        placeholder = { Text("مثال: احسب سعة ومقياس العدد ع = 1 + ت... أو اختر من الأسئلة الجاهزة أدناه") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 110.dp)
                            .testTag("question_input_field"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // 50 Questions Sheet Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isExamSheetRequested) PurpleAccent.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .clickable { isExamSheetRequested = !isExamSheetRequested }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered,
                                contentDescription = null,
                                tint = if (isExamSheetRequested) PurpleAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Column {
                                Text(
                                    text = "حل نموذج اختبار وزاري كامل (50 سؤالاً مؤتمتاً)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "25 اختيار من متعدد + 25 صح أو خطأ مع التعليل والحل المفصل",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isExamSheetRequested,
                            onCheckedChange = { isExamSheetRequested = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = PurpleAccent, checkedTrackColor = PurpleAccent.copy(alpha = 0.5f))
                        )
                    }

                    // Sample Quick-Fill Questions
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "أمثلة وزارية شائعة للحل السريع:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(sampleQuestions) { sample ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable {
                                        questionInput = sample
                                        if (sample.contains("50")) isExamSheetRequested = true
                                    }
                                ) {
                                    Text(
                                        text = sample,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Submit Solve Button
                    Button(
                        onClick = { executeSolve() },
                        enabled = !isSolving,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("solve_action_button")
                    ) {
                        if (isSolving) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جارٍ تحليل وحل المسألة...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Calculate, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isExamSheetRequested) "حل نموذج الـ 50 سؤالاً بالتفصيل" else "حل السؤال بشكل مفصل",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Solution Result View
        solutionResult?.let { solution ->
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Solution Header with Copy & Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(24.dp))
                                Column {
                                    Text(
                                        text = "الحل النموذجي المعتمد (${solution.subject})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "مستوى الدقة: ${solution.confidence}",
                                        fontSize = 11.sp,
                                        color = EmeraldSuccess,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("حل المسألة", "${solution.questionUnderstanding}\n\nالناتج النهائي: ${solution.finalAnswer}\n\n${solution.easierExplanation}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ الحل إلى الحافظة بنجاح", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "نسخ الحل", tint = NavyPrimary)
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // 1. Question Understanding
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("📌 فهم المسألة والمطلوب:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(solution.questionUnderstanding, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        // 2. Givens (المعطيات)
                        if (solution.givens.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("📋 المعطيات المستخرجة:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)
                                    solution.givens.forEach { given ->
                                        Text("• $given", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }

                        // 3. Laws & Formulas (القوانين)
                        if (solution.laws.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = NavyPrimary.copy(alpha = 0.08f)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("📐 القوانين والعلاقات الرياضية المطبقة:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)
                                    solution.laws.forEach { law ->
                                        Text("• $law", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                                    }
                                }
                            }
                        }

                        // 4. Substitution and Calculation Steps (التعويض والخطوات)
                        if (solution.substitutionSteps.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("✍️ خطوات التعويض والحل التفصيلي:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                                solution.substitutionSteps.forEachIndexed { idx, step ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = step,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(10.dp),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // 5. Final Answer Highlight
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NavyDeep),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("🎯 الناتج النهائي المعتمد:", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = solution.finalAnswer,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (solution.unitCheck.isNotBlank()) {
                                    Text(
                                        text = "فحص الوحدات: ${solution.unitCheck}",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                }
                            }
                        }

                        // 6. Simpler Teacher Explanation
                        if (solution.easierExplanation.isNotBlank()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.12f)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("💡 الشرح المبسط بأسلوب الأستاذ الخصوصي:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AmberWarning)
                                    Text(solution.easierExplanation, fontSize = 12.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }

                        // 7. Multi-question sheet list (up to 50 questions!)
                        if (solution.isExamSheet && solution.items.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "قائمة الأسئلة الـ 50 المحلولة بالتفصيل:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                solution.items.forEach { item ->
                                    val isExpanded = expandedItemNumbers.contains(item.questionNumber)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .clickable {
                                                expandedItemNumbers = if (isExpanded) {
                                                    expandedItemNumbers - item.questionNumber
                                                } else {
                                                    expandedItemNumbers + item.questionNumber
                                                }
                                            },
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .clip(CircleShape)
                                                            .background(NavyPrimary),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(item.questionNumber.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    }
                                                    Text(
                                                        text = "سؤال ${item.questionNumber}: ${item.questionType}",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }

                                                Surface(
                                                    color = if (item.isTrue == false) CrimsonSecurity.copy(alpha = 0.15f) else EmeraldSuccess.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(
                                                        text = item.finalAnswer,
                                                        color = if (item.isTrue == false) CrimsonSecurity else EmeraldSuccess,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = item.questionText,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            if (item.correction.isNotBlank()) {
                                                Text(
                                                    text = item.correction,
                                                    fontSize = 11.sp,
                                                    color = CrimsonSecurity,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            if (isExpanded) {
                                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                                                Text(
                                                    text = "التعليل والخطوات:\n${item.calculationSteps.joinToString("\n")}\n${item.easierExplanation}",
                                                    fontSize = 11.sp,
                                                    lineHeight = 16.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
