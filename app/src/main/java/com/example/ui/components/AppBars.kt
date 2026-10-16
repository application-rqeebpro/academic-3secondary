package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataRepository
import com.example.model.Screen
import com.example.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppHeader(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onOpenSecretOwnerLogin: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    var logoTapCount by remember { mutableStateOf(0) }
    val unreadNotifsCount = AppDataRepository.notifications.count { !it.isRead }

    val screenTitle = when (currentScreen) {
        Screen.HOME -> "أكاديمية رقيب"
        Screen.SUBJECTS -> "المناهج الدراسية"
        Screen.UNITS -> "الوحدات الدراسية"
        Screen.LESSON_DETAIL -> "تفاصيل الدرس والمسائل"
        Screen.SOLVE_MY_QUESTION -> "حل مسألتي الوزارية"
        Screen.SMART_TUTOR -> "المعلم الذكي الفوري"
        Screen.EXPLAIN_ME -> "اشرح لي بأبسط من كذا"
        Screen.EXAMS -> "نماذج الامتحانات الوزارية"
        Screen.EXAM_SESSION -> "جلسة الاختبار المؤتمت"
        Screen.EXAM_RESULT -> "نتيجة الامتحان الوزاري"
        Screen.QUESTION_BANK -> "بنك الأسئلة الوزارية"
        Screen.BOOKLETS -> "الملازم والملخصات"
        Screen.CALCULATOR -> "الآلة الحاسبة العلمية"
        Screen.SUBSCRIPTION -> "باقات الاشتراك والدفع"
        Screen.PAYMENT_FORM -> "إرسال إشعار السداد"
        Screen.ACTIVATE_CODE -> "تفعيل كود الاشتراك"
        Screen.PROFILE -> "حسابي"
        Screen.LOGIN -> "تسجيل الدخول"
        Screen.REGISTER -> "إنشاء حساب طالب جديد"
        Screen.SEARCH -> "البحث في المقررات والأسئلة"
        Screen.FAVORITES -> "المفضلة والمحفوظات"
        Screen.PROGRESS -> "مستوى التقدم الدراسي"
        Screen.NOTIFICATIONS -> "مركز الإشعارات والتنبيهات"
        Screen.OWNER_LOGIN -> "التحقق الأمني المركزي"
        Screen.OWNER_DASHBOARD -> "لوحة تحكم المالك"
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right side: Back button or App Logo (Secret Multi-Tap completely invisible to students)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (canNavigateBack && currentScreen != Screen.HOME) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("top_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "الرجوع",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // App Logo with R Emblem
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .combinedClickable(
                            onClick = {
                                logoTapCount++
                                if (logoTapCount >= 5) {
                                    logoTapCount = 0
                                    onOpenSecretOwnerLogin()
                                } else {
                                    onNavigate(Screen.HOME)
                                }
                            },
                            onLongClick = {
                                onOpenSecretOwnerLogin()
                            }
                        )
                ) {
                    RaqeebRLogo(size = 42.dp, showText = false)
                }

                Column {
                    Text(
                        text = screenTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (currentScreen == Screen.HOME) {
                        Text(
                            text = "افهم • حل • اختبر • تفوق",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = GoldAccent
                        )
                    }
                }
            }

            // Left side actions: Search, Notifications with Badge, Theme Toggle, Profile Avatar
            // (Completely devoid of any owner icons or clues for students!)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(onClick = { onNavigate(Screen.SEARCH) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Notifications with Unread Badge
                IconButton(onClick = { onNavigate(Screen.NOTIFICATIONS) }) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifsCount > 0) {
                                Badge(
                                    containerColor = CrimsonSecurity,
                                    contentColor = Color.White
                                ) {
                                    Text(unreadNotifsCount.toString(), fontSize = 10.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "الإشعارات",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onToggleDarkMode) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "الوضع الليلي",
                        tint = if (isDarkMode) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // If user is currently authenticated as Owner/Admin, allow returning to Dashboard
                if (AppDataRepository.isOwnerAuthenticated.value) {
                    IconButton(onClick = { onNavigate(Screen.OWNER_DASHBOARD) }) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "لوحة التحكم",
                            tint = CrimsonSecurity
                        )
                    }
                }

                // User Avatar / Profile
                val user = AppDataRepository.currentUser.value
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (user?.role == UserRole.ADMIN) CrimsonSecurity else NavyPrimary
                        )
                        .clickable { onNavigate(Screen.PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (user?.fullName?.firstOrNull() ?: 'ط').toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        Triple(Screen.HOME, "الرئيسية", Icons.Default.Home),
        Triple(Screen.SUBJECTS, "المناهج", Icons.Default.MenuBook),
        Triple(Screen.SOLVE_MY_QUESTION, "حل مسألتي", Icons.Default.AutoAwesome),
        Triple(Screen.BOOKLETS, "الملازم", Icons.Default.Description),
        Triple(Screen.PROFILE, "حسابي", Icons.Default.Person)
    )

    val showNav = items.any { it.first == currentScreen } ||
            currentScreen == Screen.EXAMS ||
            currentScreen == Screen.QUESTION_BANK ||
            currentScreen == Screen.CALCULATOR ||
            currentScreen == Screen.NOTIFICATIONS

    if (!showNav) return

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { (screen, label, icon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = NavyPrimary,
                    indicatorColor = NavyPrimary.copy(alpha = 0.12f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
