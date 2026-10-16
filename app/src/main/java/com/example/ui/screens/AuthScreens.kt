package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppDataRepository
import com.example.model.RegistrationRequest
import com.example.model.Screen
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.components.RaqeebRLogo
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigate: (Screen) -> Unit,
    onContinueAsGuest: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var phoneOrEmailInput by remember { mutableStateOf("770000000") }
    var passwordInput by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var pendingApprovalUser by remember { mutableStateOf<User?>(null) }

    fun handleLogin() {
        if (isLoading) return
        val cleanInput = phoneOrEmailInput.trim()
        val cleanPass = passwordInput.trim()

        if (cleanInput.isBlank()) {
            errorMessage = "يرجى إدخال رقم الهاتف أو البريد الإلكتروني"
            return
        }
        if (cleanPass.isBlank()) {
            errorMessage = "يرجى إدخال كلمة المرور"
            return
        }

        isLoading = true
        errorMessage = null
        focusManager.clearFocus()

        coroutineScope.launch {
            delay(500) // Smooth processing feedback
            val foundUser = AppDataRepository.users.find {
                (it.phoneOrEmail == cleanInput || it.phoneOrEmail == "0$cleanInput" || it.email.equals(cleanInput, ignoreCase = true)) &&
                        it.passwordHash == cleanPass
            }

            isLoading = false
            if (foundUser != null) {
                if (foundUser.isPendingApproval) {
                    pendingApprovalUser = foundUser
                    return@launch
                }
                if (!foundUser.isActive) {
                    errorMessage = "هذا الحساب موقوف مؤقتاً من قبل الإدارة. يرجى التواصل مع الدعم."
                    return@launch
                }

                AppDataRepository.currentUser.value = foundUser
                Toast.makeText(context, "مرحباً بك يا ${foundUser.fullName}", Toast.LENGTH_SHORT).show()

                if (foundUser.role == UserRole.ADMIN) {
                    onNavigate(Screen.OWNER_DASHBOARD)
                } else {
                    onNavigate(Screen.HOME)
                }
            } else {
                errorMessage = "رقم الهاتف أو كلمة المرور غير صحيحة. يرجى التأكد وإعادة المحاولة."
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgGradientStart, BgGradientEnd, Color.White)
                )
            )
            .testTag("login_screen")
    ) {
        val isWideScreen = maxWidth >= 760.dp

        if (isWideScreen) {
            // Tablet & Desktop 2-Column Responsive Layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Visual Inspiration Column (50%)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .shadow(16.dp, RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyTitle)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.raqeeb_study_hero_1792186641380),
                            contentDescription = "أكاديمية رقيب للتعليم الثانوي",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            NavyTitle.copy(alpha = 0.5f),
                                            NavyTitle.copy(alpha = 0.95f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(32.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = CyanInteractive.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "«مستقبلك يبدأ بخطوة»",
                                    color = CyanInteractive,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                            Text(
                                text = "أكاديمية رقيب للتعليم الثانوي",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "المنصة التعليمية المتكاملة لحل المسائل الوزارية وشرح المقررات ونماذج الامتحانات المؤتمتة لطلاب الثانوية العامة.",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                // Form Column (50%)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    LoginFormContent(
                        phoneOrEmail = phoneOrEmailInput,
                        onPhoneChange = {
                            phoneOrEmailInput = it
                            errorMessage = null
                        },
                        password = passwordInput,
                        onPasswordChange = {
                            passwordInput = it
                            errorMessage = null
                        },
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible },
                        errorMessage = errorMessage,
                        isLoading = isLoading,
                        onSubmit = { handleLogin() },
                        onForgotPassword = { showForgotPasswordDialog = true },
                        onNavigateToRegister = { onNavigate(Screen.REGISTER) },
                        onContinueAsGuest = onContinueAsGuest
                    )
                }
            }
        } else {
            // Mobile Single-Column Responsive Layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                LoginFormContent(
                    phoneOrEmail = phoneOrEmailInput,
                    onPhoneChange = {
                        phoneOrEmailInput = it
                        errorMessage = null
                    },
                    password = passwordInput,
                    onPasswordChange = {
                        passwordInput = it
                        errorMessage = null
                    },
                    passwordVisible = passwordVisible,
                    onTogglePassword = { passwordVisible = !passwordVisible },
                    errorMessage = errorMessage,
                    isLoading = isLoading,
                    onSubmit = { handleLogin() },
                    onForgotPassword = { showForgotPasswordDialog = true },
                    onNavigateToRegister = { onNavigate(Screen.REGISTER) },
                    onContinueAsGuest = onContinueAsGuest
                )
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            icon = { Icon(Icons.Default.HelpOutline, contentDescription = null, tint = RoyalBlueButton) },
            title = { Text("استعادة كلمة المرور", fontWeight = FontWeight.Bold, color = NavyTitle) },
            text = {
                Text(
                    text = "حرصاً على أمان بياناتك، يمكنك استعادة أو إعادة تعيين كلمة المرور فورياً عبر مراسلة الدعم الفني لأكاديمية رقيب عبر واتساب (785502919).",
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showForgotPasswordDialog = false
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/967785502919?text=السلام+عليكم،+أرغب+باستعادة+كلمة+المرور+لحسابي+في+أكاديمية+رقيب+للتعليم+الثانوي")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlueButton),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("مراسلة الدعم (785502919)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("إلغاء")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // Pending Approval Dialog
    pendingApprovalUser?.let { user ->
        AlertDialog(
            onDismissRequest = { pendingApprovalUser = null },
            icon = { Icon(Icons.Default.HourglassTop, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(36.dp)) },
            title = { Text("الحساب قيد المراجعة الإدارية", fontWeight = FontWeight.Bold, color = NavyTitle) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "مرحباً يا ${user.fullName}، لقد تم استلام طلب تسجيلك بنجاح وهو حالياً قيد المراجعة والاعتماد من إدارة الأكاديمية.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "لتسريع عملية التفعيل، يمكنك مراسلة المالك مباشرة عبر واتساب (785502919).",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyTitle
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        pendingApprovalUser = null
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/967785502919?text=السلام+عليكم،+أنا+الطالب+${Uri.encode(user.fullName)}+أرجو+تفعيل+حسابي+المسجل")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("تواصل عبر واتساب للتفعيل", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingApprovalUser = null }) {
                    Text("حسناً")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun LoginFormContent(
    phoneOrEmail: String,
    onPhoneChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePassword: () -> Unit,
    errorMessage: String?,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 440.dp)
            .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = RoyalBlueButton.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo with the letter R and education symbol
            RaqeebRLogo(size = 72.dp, showText = false)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "مرحبًا بعودتك",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = NavyTitle
                )
                Text(
                    text = "سجّل دخولك وواصل رحلتك نحو التفوق",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = LightOnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Floating Decorative Academic Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("📐 رياضيات", "⚡ فيزياء", "🧪 كيمياء", "🧬 أحياء").forEach { tag ->
                    Surface(
                        color = BgGradientEnd,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(horizontal = 3.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyTitle,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Input: Phone or Email
            OutlinedTextField(
                value = phoneOrEmail,
                onValueChange = onPhoneChange,
                label = { Text("رقم الهاتف أو وسيلة الدخول") },
                placeholder = { Text("مثال: 770000000") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PhoneIphone,
                        contentDescription = null,
                        tint = if (phoneOrEmail.isNotBlank()) RoyalBlueButton else LightOnSurfaceVariant
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalBlueButton,
                    unfocusedBorderColor = Color(0xFFD4E2F7),
                    focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = RoyalBlueButton
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_phone_field")
            )

            // Input: Password
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("كلمة المرور") },
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (password.isNotBlank()) RoyalBlueButton else LightOnSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = onTogglePassword) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "إخفاء كلمة المرور" else "إظهار كلمة المرور",
                                tint = RoyalBlueButton
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalBlueButton,
                        unfocusedBorderColor = Color(0xFFD4E2F7),
                        focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = RoyalBlueButton
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_field")
                )

                // Forgot Password link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "نسيت كلمة المرور؟",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlueButton,
                        modifier = Modifier.clickable { onForgotPassword() }
                    )
                }
            }

            // Error Message Banner
            if (errorMessage != null) {
                Surface(
                    color = SoftErrorRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(SoftErrorRed.copy(alpha = 0.4f), SoftErrorRed))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = SoftErrorRed, modifier = Modifier.size(18.dp))
                        Text(
                            text = errorMessage,
                            color = SoftErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Primary Login Button
            Button(
                onClick = onSubmit,
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoyalBlueButton,
                    disabledContainerColor = RoyalBlueButton.copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_submit_btn")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("جارٍ التحقق والدخول...", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("تسجيل الدخول", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Switch to Register
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ليس لديك حساب؟ ",
                    fontSize = 13.sp,
                    color = LightOnSurfaceVariant
                )
                Text(
                    text = "إنشاء حساب جديد",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlueButton,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            HorizontalDivider(
                color = Color(0xFFE2ECFA),
                modifier = Modifier.padding(vertical = 2.dp)
            )

            // Continue as guest
            OutlinedButton(
                onClick = onContinueAsGuest,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyTitle),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(Color(0xFFD4E2F7), Color(0xFFD4E2F7)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Explore, contentDescription = null, tint = CyanInteractive, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("المتابعة والتصفح كزائر", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var fullName by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var universityOrSchool by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var selectedGrade by remember { mutableStateOf("الثالث الثانوي") }
    var selectedTrack by remember { mutableStateOf("علمي") }
    var termsAccepted by remember { mutableStateOf(true) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var errorField by remember { mutableStateOf<String?>(null) }

    // Curated high schools in Yemen
    val popularSchools = listOf(
        "ثانوية جمال عبد الناصر للمتفوقين",
        "ثانوية الكويت النموذجية",
        "ثانوية عدن النموذجية",
        "ثانوية بلقيس للبنات",
        "ثانوية تعز الكبرى",
        "ثانوية المكلا النموذجية",
        "أخرى (كتابة يدوية)"
    )

    fun handleRegister() {
        if (isLoading) return

        if (fullName.isBlank()) {
            errorMessage = "يرجى إدخال اسم الطالب الكامل"
            errorField = "name"
            return
        }
        val cleanPhone = phoneInput.trim().replace("+967", "").replace(" ", "")
        if (cleanPhone.length < 8) {
            errorMessage = "يرجى إدخال رقم هاتف يمني صحيح (8 أو 9 أرقام)"
            errorField = "phone"
            return
        }
        if (passwordInput.length < 6) {
            errorMessage = "يجب ألا تقل كلمة المرور عن 6 أحرف أو أرقام"
            errorField = "password"
            return
        }
        if (passwordInput != confirmPasswordInput) {
            errorMessage = "كلمة المرور وتأكيدها غير متطابقين"
            errorField = "confirm"
            return
        }
        if (!termsAccepted) {
            errorMessage = "يرجى الموافقة على شروط الاستخدام للمتابعة"
            errorField = "terms"
            return
        }

        // Duplicate phone check
        val phoneExists = AppDataRepository.users.any { it.phoneOrEmail == cleanPhone } ||
                AppDataRepository.registrationRequests.any { it.phone == cleanPhone }
        if (phoneExists) {
            errorMessage = "رقم الهاتف مسجل بالفعل في الأكاديمية. يرجى تسجيل الدخول أو استخدام رقم آخر."
            errorField = "phone"
            return
        }

        isLoading = true
        errorMessage = null
        focusManager.clearFocus()

        coroutineScope.launch {
            delay(600)
            val newId = System.currentTimeMillis()
            val schoolFinal = universityOrSchool.trim().ifEmpty { "ثانوية عامة" }

            // 1. Add to Registration Requests for Admin Approval tracking
            AppDataRepository.registrationRequests.add(
                0,
                RegistrationRequest(
                    id = newId,
                    fullName = fullName.trim(),
                    phone = cleanPhone,
                    email = emailInput.trim(),
                    passwordHash = passwordInput.trim(),
                    universityOrSchool = schoolFinal,
                    grade = selectedGrade,
                    track = selectedTrack,
                    status = "PENDING"
                )
            )

            // 2. Add as active student account
            val newStudent = User(
                id = newId,
                fullName = fullName.trim(),
                phoneOrEmail = cleanPhone,
                email = emailInput.trim(),
                universityOrSchool = schoolFinal,
                passwordHash = passwordInput.trim(),
                role = UserRole.STUDENT, // Strictly STUDENT, NEVER ADMIN
                grade = selectedGrade,
                track = selectedTrack,
                isActive = true,
                isPendingApproval = false
            )
            AppDataRepository.users.add(newStudent)
            AppDataRepository.currentUser.value = newStudent

            isLoading = false
            Toast.makeText(context, "تم إنشاء الحساب بنجاح! أهلاً بك يا ${newStudent.fullName}", Toast.LENGTH_LONG).show()
            onNavigate(Screen.HOME)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgGradientStart, BgGradientEnd, Color.White)
                )
            )
            .testTag("register_screen")
    ) {
        val isWide = maxWidth >= 760.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = if (isWide) 640.dp else 460.dp)
                    .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = RoyalBlueButton.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    RaqeebRLogo(size = 64.dp, showText = false)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "أنشئ حسابك الآن",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = NavyTitle
                        )
                        Text(
                            text = "انضم إلى نخبة طلاب الثانوية العامة في أكاديمية رقيب",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightOnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Field: Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            errorMessage = null
                        },
                        label = { Text("الاسم الكامل للطالب") },
                        placeholder = { Text("مثال: عبد الرحمن محمد أحمد") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RoyalBlueButton) },
                        singleLine = true,
                        isError = errorField == "name",
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlueButton,
                            unfocusedBorderColor = Color(0xFFD4E2F7),
                            focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Field: Phone
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = {
                            phoneInput = it
                            errorMessage = null
                        },
                        label = { Text("رقم الهاتف (اليمن)") },
                        placeholder = { Text("مثال: 771234567") },
                        leadingIcon = { Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = RoyalBlueButton) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        isError = errorField == "phone",
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlueButton,
                            unfocusedBorderColor = Color(0xFFD4E2F7),
                            focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Field: Email (Optional)
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = {
                            emailInput = it
                            errorMessage = null
                        },
                        label = { Text("البريد الإلكتروني (اختياري)") },
                        placeholder = { Text("student@gmail.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = RoyalBlueButton) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlueButton,
                            unfocusedBorderColor = Color(0xFFD4E2F7),
                            focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Field: School / High School
                    OutlinedTextField(
                        value = universityOrSchool,
                        onValueChange = {
                            universityOrSchool = it
                            errorMessage = null
                        },
                        label = { Text("المدرسة / الثانوية أو الجامعة") },
                        placeholder = { Text("اختر أو اكتب اسم المدرسة") },
                        leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = RoyalBlueButton) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlueButton,
                            unfocusedBorderColor = Color(0xFFD4E2F7),
                            focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Quick school chips selector
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(popularSchools) { sch ->
                            Surface(
                                color = if (universityOrSchool == sch) RoyalBlueButton.copy(alpha = 0.12f) else BgGradientStart,
                                shape = RoundedCornerShape(8.dp),
                                border = if (universityOrSchool == sch) ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(RoyalBlueButton, RoyalBlueButton))) else null,
                                modifier = Modifier.clickable {
                                    universityOrSchool = if (sch.contains("أخرى")) "" else sch
                                }
                            ) {
                                Text(
                                    text = sch,
                                    fontSize = 11.sp,
                                    color = if (universityOrSchool == sch) RoyalBlueButton else NavyTitle,
                                    fontWeight = if (universityOrSchool == sch) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Grade & Track Choice
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("علمي", "أدبي").forEach { track ->
                            val isSel = selectedTrack == track
                            Button(
                                onClick = { selectedTrack = track },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSel) RoyalBlueButton else BgGradientStart,
                                    contentColor = if (isSel) Color.White else NavyTitle
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("الشعبة $track", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    // Password and Confirm Password (side-by-side on wide screens)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                errorMessage = null
                            },
                            label = { Text("كلمة المرور") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RoyalBlueButton) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = RoyalBlueButton
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            isError = errorField == "password",
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlueButton,
                                unfocusedBorderColor = Color(0xFFD4E2F7),
                                focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = confirmPasswordInput,
                            onValueChange = {
                                confirmPasswordInput = it
                                errorMessage = null
                            },
                            label = { Text("تأكيد المرور") },
                            leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null, tint = RoyalBlueButton) },
                            trailingIcon = {
                                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                    Icon(
                                        imageVector = if (confirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = RoyalBlueButton
                                    )
                                }
                            },
                            visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            isError = errorField == "confirm",
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlueButton,
                                unfocusedBorderColor = Color(0xFFD4E2F7),
                                focusedContainerColor = BgGradientStart.copy(alpha = 0.4f),
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Terms Checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { termsAccepted = !termsAccepted },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { termsAccepted = it },
                            colors = CheckboxDefaults.colors(checkedColor = RoyalBlueButton)
                        )
                        Text(
                            text = "أوافق على شروط الاستخدام وسياسة الخصوصية لمنصة رقيب",
                            fontSize = 12.sp,
                            color = NavyTitle,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Error Message
                    if (errorMessage != null) {
                        Surface(
                            color = SoftErrorRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = SoftErrorRed, modifier = Modifier.size(18.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    color = SoftErrorRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Register Action Button
                    Button(
                        onClick = { handleRegister() },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalBlueButton,
                            disabledContainerColor = RoyalBlueButton.copy(alpha = 0.6f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("register_submit_btn")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("جارٍ إنشاء الحساب...", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("إنشاء حساب والبدء الآن", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Back to Login link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "لديك حساب بالفعل؟ ",
                            fontSize = 13.sp,
                            color = LightOnSurfaceVariant
                        )
                        Text(
                            text = "تسجيل الدخول",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlueButton,
                            modifier = Modifier.clickable { onNavigate(Screen.LOGIN) }
                        )
                    }
                }
            }
        }
    }
}
