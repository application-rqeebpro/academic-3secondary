package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataRepository
import com.example.model.Lesson
import com.example.model.Screen
import com.example.ui.theme.NavyPrimary

@Composable
fun SearchScreen(
    onSelectLesson: (Lesson) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val clean = searchQuery.trim().lowercase()

    // Secret owner trigger code: *#785502# or *#owner#
    LaunchedEffect(clean) {
        if (clean == "*#785502#" || clean == "*#owner#" || clean == "*#785502919#") {
            onNavigate(Screen.OWNER_LOGIN)
        }
    }

    val matchedLessons = if (clean.isNotBlank()) {
        AppDataRepository.lessons.filter {
            it.title.lowercase().contains(clean) ||
                    it.coreIdea.lowercase().contains(clean) ||
                    it.simplifiedExplanation.lowercase().contains(clean) ||
                    it.formulas.lowercase().contains(clean)
        }
    } else emptyList()

    val matchedQuestions = if (clean.isNotBlank()) {
        AppDataRepository.questions.filter {
            it.questionText.lowercase().contains(clean) ||
                    it.explanation.lowercase().contains(clean)
        }
    } else emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("search_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("بحث شامل في الدروس، القوانين، والأسئلة الوزارية...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (clean.isBlank()) {
            item {
                Text(
                    text = "جرب البحث عن: ديموافر، مقذوفات، اتزان كيميائي، وراثة، تفاضل...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            item {
                Text(
                    text = "نتائج البحث (${matchedLessons.size + matchedQuestions.size}):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            items(matchedLessons) { lesson ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectLesson(lesson)
                            onNavigate(Screen.LESSON_DETAIL)
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("درس: ${lesson.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Text(lesson.coreIdea, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                    }
                }
            }

            items(matchedQuestions) { q ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("سؤال وزاري: ${q.questionText}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("الخيار المعتمد: ${q.correctOption}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
