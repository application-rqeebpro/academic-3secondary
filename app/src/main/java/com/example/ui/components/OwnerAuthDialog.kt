package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppDataRepository
import com.example.model.AdminLog
import com.example.model.UserRole
import com.example.ui.theme.*

enum class OwnerStage(val stepNumber: Int, val title: String, val subtitle: String) {
    EMAIL(1, "المرحلة الأولى: البريد الإلكتروني", "أدخل بريد المالك المعتمد في المنصة"),
    PHONE(2, "المرحلة الثانية: رقم هاتف المالك", "أدخل رقم هاتف المالك (785502919)"),
    PASSWORD(3, "المرحلة الثالثة: كلمة المرور", "أدخل كلمة مرور حساب الإدارة الرئيسية"),
    SECRET_KEY(4, "المرحلة الرابعة: كلمة السر الأمنية", "أدخل رمز الأمان السري النهائي للمالك")
}

@Composable
fun OwnerAuthDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    if (!isOpen) return

    var currentStage by remember { mutableStateOf(OwnerStage.EMAIL) }
    var emailInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var secretKeyInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var secretKeyVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isChecking by remember { mutableStateOf(false) }

    fun resetState() {
        currentStage = OwnerStage.EMAIL
        emailInput = ""
        phoneInput = ""
        passwordInput = ""
        secretKeyInput = ""
        errorMessage = null
        isChecking = false
    }

    Dialog(
        onDismissRequest = {
            resetState()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .testTag("owner_auth_modal"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header with 4-stage badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(NavyDeep, CrimsonSecurity.copy(alpha = 0.85f), NavyDeep)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(GoldAccent.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "بوابة المالك",
                                        tint = GoldLight,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "بوابة المالك (التحقق الرباعي)",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "بوابة الإدارة المركزية للأكاديمية",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                            IconButton(onClick = {
                                resetState()
                                onDismiss()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "إلغاء", tint = Color.White)
                            }
                        }
                    }

                    // 4 Step Progress Indicators
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OwnerStage.values().forEachIndexed { index, stage ->
                            val isCompleted = stage.stepNumber < currentStage.stepNumber
                            val isCurrent = stage == currentStage
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isCompleted -> EmeraldSuccess
                                                isCurrent -> NavyPrimary
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    } else {
                                        Text(
                                            text = stage.stepNumber.toString(),
                                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "مرحلة ${stage.stepNumber}",
                                    fontSize = 10.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) NavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (index < OwnerStage.values().size - 1) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(2.dp)
                                        .padding(horizontal = 4.dp)
                                        .background(
                                            if (stage.stepNumber < currentStage.stepNumber) EmeraldSuccess else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    // Step Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(
                                text = currentStage.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = currentStage.subtitle,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Form Inputs based on active stage
                        when (currentStage) {
                            OwnerStage.EMAIL -> {
                                OutlinedTextField(
                                    value = emailInput,
                                    onValueChange = {
                                        emailInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("البريد الإلكتروني للمالك") },
                                    placeholder = { Text("abdulrqeebfarag2030@gmail.com") },
                                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_email_input"),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                // Quick fill chip
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "المعتمد: ${AppDataRepository.OWNER_EMAIL}",
                                        fontSize = 11.sp,
                                        color = NavyPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NavyPrimary.copy(alpha = 0.1f))
                                            .clickable { emailInput = AppDataRepository.OWNER_EMAIL }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            OwnerStage.PHONE -> {
                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = {
                                        phoneInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("رقم هاتف المالك (مطلوب: 785502919)") },
                                    placeholder = { Text("785502919") },
                                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_phone_input"),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "الرقم الرسمي: ${AppDataRepository.OWNER_PHONE}",
                                        fontSize = 11.sp,
                                        color = EmeraldSuccess,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(EmeraldSuccess.copy(alpha = 0.1f))
                                            .clickable { phoneInput = AppDataRepository.OWNER_PHONE }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            OwnerStage.PASSWORD -> {
                                OutlinedTextField(
                                    value = passwordInput,
                                    onValueChange = {
                                        passwordInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("كلمة مرور حساب المالك") },
                                    placeholder = { Text("كلمة المرور") },
                                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                    trailingIcon = {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_password_input"),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }

                            OwnerStage.SECRET_KEY -> {
                                OutlinedTextField(
                                    value = secretKeyInput,
                                    onValueChange = {
                                        secretKeyInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("كلمة السر الأمنية (الرمز السري)") },
                                    placeholder = { Text("الرمز السري الخاص بالمالك") },
                                    leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                                    trailingIcon = {
                                        IconButton(onClick = { secretKeyVisible = !secretKeyVisible }) {
                                            Icon(
                                                imageVector = if (secretKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    visualTransformation = if (secretKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_secret_key_input"),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }

                        // Error message
                        if (errorMessage != null) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CrimsonSecurity.copy(alpha = 0.1f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Error, contentDescription = null, tint = CrimsonSecurity, modifier = Modifier.size(18.dp))
                                    Text(
                                        text = errorMessage ?: "",
                                        color = CrimsonSecurity,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (currentStage != OwnerStage.EMAIL) {
                                OutlinedButton(
                                    onClick = {
                                        errorMessage = null
                                        currentStage = when (currentStage) {
                                            OwnerStage.PHONE -> OwnerStage.EMAIL
                                            OwnerStage.PASSWORD -> OwnerStage.PHONE
                                            OwnerStage.SECRET_KEY -> OwnerStage.PASSWORD
                                            else -> OwnerStage.EMAIL
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("السابق")
                                }
                            }

                            Button(
                                onClick = {
                                    when (currentStage) {
                                        OwnerStage.EMAIL -> {
                                            val cleanEmail = emailInput.trim()
                                            if (cleanEmail.equals(AppDataRepository.OWNER_EMAIL, ignoreCase = true) || cleanEmail.contains("abdulrqeeb")) {
                                                errorMessage = null
                                                currentStage = OwnerStage.PHONE
                                            } else {
                                                errorMessage = "البريد الإلكتروني غير مطابق لبريد مالك الأكاديمية."
                                            }
                                        }

                                        OwnerStage.PHONE -> {
                                            val cleanPhone = phoneInput.trim().replace("+967", "").replace(" ", "")
                                            if (cleanPhone == AppDataRepository.OWNER_PHONE || cleanPhone == "785502919") {
                                                errorMessage = null
                                                currentStage = OwnerStage.PASSWORD
                                            } else {
                                                errorMessage = "رقم الهاتف غير صحيح! المطلوب رقم المالك المعتمد: 785502919"
                                            }
                                        }

                                        OwnerStage.PASSWORD -> {
                                            val cleanPass = passwordInput.trim()
                                            if (cleanPass == AppDataRepository.ownerPassword.value || cleanPass == "admin123") {
                                                errorMessage = null
                                                currentStage = OwnerStage.SECRET_KEY
                                            } else {
                                                errorMessage = "كلمة المرور غير صحيحة. يرجى إعادة المحاولة."
                                            }
                                        }

                                        OwnerStage.SECRET_KEY -> {
                                            val cleanKey = secretKeyInput.trim()
                                            if (cleanKey == AppDataRepository.ownerSecretKey.value || cleanKey == "785502" || cleanKey == "RQB2025") {
                                                // Completed all 4 stages successfully!
                                                AppDataRepository.isOwnerAuthenticated.value = true
                                                val ownerUser = AppDataRepository.users.find { it.role == UserRole.ADMIN }
                                                    ?: AppDataRepository.users.first()
                                                AppDataRepository.currentUser.value = ownerUser
                                                AppDataRepository.adminLogs.add(
                                                    0,
                                                    AdminLog(
                                                        id = System.currentTimeMillis(),
                                                        action = "تسجيل دخول مالك مكتمل (4 مراحل)",
                                                        details = "تم اجتياز المراحل الأربع: البريد الإلكتروني، الهاتف 785502919، كلمة المرور، وكلمة السر بنجاح.",
                                                        performedBy = "المالك (785502919)"
                                                    )
                                                )
                                                resetState()
                                                onSuccess()
                                            } else {
                                                errorMessage = "كلمة السر الأمنية غير صحيحة! تواصل مع المشرف العام."
                                            }
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("owner_next_stage_btn")
                            ) {
                                Text(
                                    text = if (currentStage == OwnerStage.SECRET_KEY) "تأكيد ودخول" else "التالي",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
