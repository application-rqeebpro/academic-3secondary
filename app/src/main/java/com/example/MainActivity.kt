package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.AppDataRepository
import com.example.model.*
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val systemDark = isSystemInDarkTheme()
            var userChangedTheme by remember { mutableStateOf(false) }
            val activeDarkMode = if (userChangedTheme) isDarkMode else systemDark

            MyApplicationTheme(darkTheme = activeDarkMode) {
                // If user is not logged in, show LOGIN first as requested!
                val initialScreen = if (AppDataRepository.currentUser.value == null) Screen.LOGIN else Screen.HOME
                val screenStack = remember { mutableStateListOf(initialScreen) }
                val currentScreen = screenStack.lastOrNull() ?: Screen.LOGIN

                var selectedSubject by remember { mutableStateOf(AppDataRepository.subjects.first()) }
                var selectedLesson by remember { mutableStateOf(AppDataRepository.lessons.first()) }
                var activeExam by remember { mutableStateOf(AppDataRepository.exams.first()) }
                var lastExamResult by remember { mutableStateOf<ExamResult?>(null) }

                fun navigateTo(screen: Screen) {
                    if (screenStack.lastOrNull() != screen) {
                        screenStack.add(screen)
                    }
                }

                fun navigateBack() {
                    if (screenStack.size > 1) {
                        screenStack.removeAt(screenStack.lastIndex)
                    }
                }

                // Handle Hardware and Gestural Back Button
                BackHandler(enabled = screenStack.size > 1) {
                    navigateBack()
                }

                val isAuthFlow = currentScreen == Screen.LOGIN || currentScreen == Screen.REGISTER || currentScreen == Screen.OWNER_LOGIN

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isAuthFlow) {
                            AppHeader(
                                currentScreen = currentScreen,
                                canNavigateBack = screenStack.size > 1,
                                onNavigateBack = { navigateBack() },
                                onNavigate = { navigateTo(it) },
                                onOpenSecretOwnerLogin = { navigateTo(Screen.OWNER_LOGIN) },
                                isDarkMode = activeDarkMode,
                                onToggleDarkMode = {
                                    userChangedTheme = true
                                    isDarkMode = !activeDarkMode
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (!isAuthFlow && currentScreen != Screen.OWNER_DASHBOARD) {
                            AppBottomNav(
                                currentScreen = currentScreen,
                                onNavigate = { navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(if (isAuthFlow) androidx.compose.foundation.layout.PaddingValues() else innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.LOGIN -> LoginScreen(
                                onNavigate = { navigateTo(it) },
                                onContinueAsGuest = {
                                    // Guest access
                                    AppDataRepository.currentUser.value = User(
                                        id = 999L,
                                        fullName = "طالب زائر",
                                        phoneOrEmail = "زائر",
                                        passwordHash = "",
                                        role = UserRole.STUDENT
                                    )
                                    navigateTo(Screen.HOME)
                                }
                            )

                            Screen.REGISTER -> RegisterScreen(
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.OWNER_LOGIN -> OwnerLoginScreen(
                                onLoginSuccess = {
                                    navigateTo(Screen.OWNER_DASHBOARD)
                                },
                                onCancel = {
                                    navigateBack()
                                }
                            )

                            Screen.HOME -> HomeScreen(
                                onNavigate = { navigateTo(it) },
                                onSelectSubject = {
                                    selectedSubject = it
                                    navigateTo(Screen.UNITS)
                                }
                            )

                            Screen.SUBJECTS -> SubjectsScreen(
                                onSelectSubject = {
                                    selectedSubject = it
                                    navigateTo(Screen.UNITS)
                                },
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.UNITS -> UnitsScreen(
                                subject = selectedSubject,
                                onSelectLesson = {
                                    selectedLesson = it
                                    navigateTo(Screen.LESSON_DETAIL)
                                },
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.LESSON_DETAIL -> LessonDetailScreen(
                                lesson = selectedLesson
                            )

                            Screen.SOLVE_MY_QUESTION -> SolveMyQuestionScreen()

                            Screen.SMART_TUTOR -> SmartTutorScreen()

                            Screen.EXPLAIN_ME -> SolveMyQuestionScreen()

                            Screen.EXAMS -> ExamsScreen(
                                onStartExam = {
                                    activeExam = it
                                    navigateTo(Screen.EXAM_SESSION)
                                },
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.EXAM_SESSION -> ExamSessionScreen(
                                exam = activeExam,
                                onSubmitExam = { result ->
                                    lastExamResult = result
                                    navigateTo(Screen.EXAM_RESULT)
                                }
                            )

                            Screen.EXAM_RESULT -> lastExamResult?.let { res ->
                                ExamResultScreen(
                                    result = res,
                                    onNavigate = { navigateTo(it) }
                                )
                            } ?: HomeScreen(
                                onNavigate = { navigateTo(it) },
                                onSelectSubject = { selectedSubject = it }
                            )

                            Screen.QUESTION_BANK -> QuestionBankScreen()

                            Screen.BOOKLETS -> BookletsScreen()

                            Screen.CALCULATOR -> ScientificCalculatorScreen()

                            Screen.SUBSCRIPTION -> SubscriptionScreen(
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.PAYMENT_FORM -> PaymentFormScreen(
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.ACTIVATE_CODE -> ActivateCodeScreen(
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.NOTIFICATIONS -> NotificationScreen()

                            Screen.PROFILE -> ProfileScreen(
                                onNavigate = { navigateTo(it) },
                                onLogout = {
                                    AppDataRepository.logout()
                                    screenStack.clear()
                                    screenStack.add(Screen.LOGIN)
                                },
                                onSecretOwnerTrigger = {
                                    navigateTo(Screen.OWNER_LOGIN)
                                }
                            )

                            Screen.SEARCH -> SearchScreen(
                                onSelectLesson = {
                                    selectedLesson = it
                                    navigateTo(Screen.LESSON_DETAIL)
                                },
                                onNavigate = { navigateTo(it) }
                            )

                            Screen.FAVORITES -> HomeScreen(
                                onNavigate = { navigateTo(it) },
                                onSelectSubject = { selectedSubject = it }
                            )

                            Screen.PROGRESS -> HomeScreen(
                                onNavigate = { navigateTo(it) },
                                onSelectSubject = { selectedSubject = it }
                            )

                            Screen.OWNER_DASHBOARD -> OwnerDashboardScreen(
                                onLogout = {
                                    AppDataRepository.logout()
                                    screenStack.clear()
                                    screenStack.add(Screen.LOGIN)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
