package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataRepository
import com.example.model.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OwnerDashboardScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var selectedSection by remember { mutableStateOf(0) }

    // 13 Official Administrative Sections as requested
    val sections = listOf(
        "1. الرئيسية والإحصاءات",
        "2. إدارة الطلاب",
        "3. طلبات التسجيل",
        "4. الاشتراكات",
        "5. أكواد التفعيل",
        "6. الكتب والملازم",
        "7. النماذج والملخصات",
        "8. الأسئلة والاختبارات",
        "9. مركز الإشعارات",
        "10. إدارة المحتوى",
        "11. إعدادات التطبيق",
        "12. سجل العمليات",
        "13. حساب المالك والخروج"
    )

    val sdf = remember { SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar")) }

    // Stats calculations
    val totalStudents = AppDataRepository.users.count { it.role == UserRole.STUDENT }
    val activeStudents = AppDataRepository.users.count { it.role == UserRole.STUDENT && it.isActive }
    val suspendedStudents = AppDataRepository.users.count { it.role == UserRole.STUDENT && !it.isActive }
    val pendingRegistrations = AppDataRepository.registrationRequests.count { it.status == "PENDING" }
    val activeSubs = AppDataRepository.subscriptions.count { it.isActive && it.endDate > System.currentTimeMillis() }
    val expiredSubs = AppDataRepository.subscriptions.count { !it.isActive || it.endDate <= System.currentTimeMillis() }
    val totalBooklets = AppDataRepository.booklets.size
    val totalSummaries = AppDataRepository.summaries.size
    val totalQuestions = AppDataRepository.questions.size
    val totalExams = AppDataRepository.exams.size
    val availableCodes = AppDataRepository.subscriptionCodes.count { !it.isUsed }
    val usedCodes = AppDataRepository.subscriptionCodes.count { it.isUsed }

    // Form States
    // Student Form
    var newStudentName by remember { mutableStateOf("") }
    var newStudentPhone by remember { mutableStateOf("") }
    var newStudentSchool by remember { mutableStateOf("") }
    var newStudentPassword by remember { mutableStateOf("123456") }
    var studentSearchQuery by remember { mutableStateOf("") }

    // Codes Form
    var codeCountToGenerate by remember { mutableStateOf(1) }
    var codeDaysSelection by remember { mutableStateOf(90) }
    var codeCustomPrefix by remember { mutableStateOf("RQB") }

    // Booklet Form
    var bookletTitle by remember { mutableStateOf("") }
    var bookletSubject by remember { mutableStateOf("الرياضيات") }
    var bookletType by remember { mutableStateOf("PDF") }
    var bookletIsFree by remember { mutableStateOf(false) }

    // Summary Review Form
    var summaryTitle by remember { mutableStateOf("") }
    var summarySubject by remember { mutableStateOf("الرياضيات") }
    var summaryType by remember { mutableStateOf("ملخص درس") }

    // Question Form
    var qText by remember { mutableStateOf("") }
    var qSubjectId by remember { mutableStateOf(1) }
    var qType by remember { mutableStateOf("MULTIPLE_CHOICE") }
    var qOptA by remember { mutableStateOf("") }
    var qOptB by remember { mutableStateOf("") }
    var qOptC by remember { mutableStateOf("") }
    var qOptD by remember { mutableStateOf("") }
    var qCorrect by remember { mutableStateOf("A") }
    var qExplanation by remember { mutableStateOf("") }

    // Notification Form
    var notifTitle by remember { mutableStateOf("") }
    var notifMessage by remember { mutableStateOf("") }
    var notifType by remember { mutableStateOf("إعلان عام") }
    var notifAudience by remember { mutableStateOf("جميع الطلاب") }

    // Security Form
    var currentPasswordEdit by remember { mutableStateOf(AppDataRepository.ownerPassword.value) }
    var currentSecretEdit by remember { mutableStateOf(AppDataRepository.ownerSecretKey.value) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("owner_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Owner Identity Header
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(GoldAccent.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldLight)
                                }
                                Column {
                                    Text(
                                        text = "لوحة تحكم المالك — الصلاحيات الكاملة",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "المالك: ${AppDataRepository.OWNER_EMAIL} | ${AppDataRepository.OWNER_PHONE}",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                            Button(
                                onClick = onLogout,
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecurity),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("خروج", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section Tabs Selector
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedSection,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = NavyPrimary,
                edgePadding = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                sections.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSection == index,
                        onClick = { selectedSection = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // 1. الرئيسية والإحصاءات
        if (selectedSection == 0) {
            item {
                Text("الإحصائيات والأرقام الحقيقية لقاعدة البيانات:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("إجمالي الطلاب", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalStudents طالب", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                Text("نشط: $activeStudents | موقوف: $suspendedStudents", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("طلبات التسجيل المعلقة", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$pendingRegistrations طلب", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                                Text("تنتظر مراجعة المالك", fontSize = 10.sp, color = AmberWarning)
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("الاشتراكات", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$activeSubs نشط", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                Text("منتهي: $expiredSubs", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("أكواد التفعيل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$availableCodes متاح", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PurpleAccent)
                                Text("مستخدم: $usedCodes", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("الكتب والملازم", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalBooklets ملزمة", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                Text("النماذج والملخصات: $totalSummaries", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("الأسئلة والاختبارات", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalQuestions سؤالاً", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                Text("الاختبارات الوزارية: $totalExams", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            item {
                Text("آخر العمليات الإدارية المسجلة:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            items(AppDataRepository.adminLogs.take(5)) { log ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(log.action, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(log.details, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(sdf.format(Date(log.timestamp)), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // 2. إدارة الطلاب
        if (selectedSection == 1) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إضافة طالب جديد يدوياً:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(value = newStudentName, onValueChange = { newStudentName = it }, label = { Text("الاسم الكامل") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        OutlinedTextField(value = newStudentPhone, onValueChange = { newStudentPhone = it }, label = { Text("رقم الهاتف") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        OutlinedTextField(value = newStudentSchool, onValueChange = { newStudentSchool = it }, label = { Text("المدرسة / الجامعة") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                        Button(
                            onClick = {
                                if (newStudentName.isNotBlank() && newStudentPhone.isNotBlank()) {
                                    val newUser = User(
                                        id = System.currentTimeMillis(),
                                        fullName = newStudentName.trim(),
                                        phoneOrEmail = newStudentPhone.trim(),
                                        universityOrSchool = newStudentSchool.trim().ifEmpty { "ثانوية المتفوقين" },
                                        passwordHash = newStudentPassword.trim(),
                                        role = UserRole.STUDENT,
                                        grade = "الثالث الثانوي",
                                        track = "علمي",
                                        isActive = true
                                    )
                                    AppDataRepository.users.add(newUser)
                                    AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "إضافة طالب يدوياً", "تمت إضافة الطالب ${newUser.fullName}", "المالك"))
                                    newStudentName = ""
                                    newStudentPhone = ""
                                    newStudentSchool = ""
                                    Toast.makeText(context, "تمت إضافة الطالب بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("حفظ الحساب")
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = studentSearchQuery,
                    onValueChange = { studentSearchQuery = it },
                    label = { Text("بحث عن طالب بالاسم أو الهاتف...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            items(AppDataRepository.users.filter { it.role == UserRole.STUDENT && (studentSearchQuery.isBlank() || it.fullName.contains(studentSearchQuery) || it.phoneOrEmail.contains(studentSearchQuery)) }) { student ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(student.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("الهاتف: ${student.phoneOrEmail} | ${student.universityOrSchool}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                color = if (student.isActive) EmeraldSuccess.copy(alpha = 0.15f) else CrimsonSecurity.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (student.isActive) "نشط" else "موقوف",
                                    color = if (student.isActive) EmeraldSuccess else CrimsonSecurity,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val index = AppDataRepository.users.indexOf(student)
                                    if (index != -1) {
                                        AppDataRepository.users[index] = student.copy(isActive = !student.isActive)
                                        AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "تعديل حالة حساب", "تم تغيير حالة الطالب ${student.fullName} إلى ${if (!student.isActive) "نشط" else "موقوف"}", "المالك"))
                                        Toast.makeText(context, "تم تغيير حالة الحساب", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (student.isActive) "إيقاف الحساب" else "تفعيل الحساب", fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    AppDataRepository.users.remove(student)
                                    AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "حذف حساب طالب", "تم حذف حساب الطالب ${student.fullName}", "المالك"))
                                    Toast.makeText(context, "تم حذف الحساب", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecurity),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("حذف الحساب", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. طلبات التسجيل
        if (selectedSection == 2) {
            item {
                Text("طلبات تسجيل الطلاب المعلقة والمقدمة عبر التطبيق:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            if (AppDataRepository.registrationRequests.isEmpty()) {
                item { Text("لا توجد طلبات تسجيل حالياً.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(AppDataRepository.registrationRequests) { req ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(req.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Surface(
                                    color = when (req.status) {
                                        "APPROVED" -> EmeraldSuccess.copy(alpha = 0.15f)
                                        "REJECTED" -> CrimsonSecurity.copy(alpha = 0.15f)
                                        else -> AmberWarning.copy(alpha = 0.15f)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when (req.status) {
                                            "APPROVED" -> "معتمد ✔"
                                            "REJECTED" -> "مرفوض"
                                            else -> "قيد المراجعة"
                                        },
                                        color = when (req.status) {
                                            "APPROVED" -> EmeraldSuccess
                                            "REJECTED" -> CrimsonSecurity
                                            else -> AmberWarning
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text("الهاتف: ${req.phone} | البريد: ${req.email}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("المدرسة/الجامعة: ${req.universityOrSchool} | القسم: ${req.track}", fontSize = 11.sp)

                            if (req.status == "PENDING") {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            AppDataRepository.approveRegistration(req.id)
                                            Toast.makeText(context, "تم قبول الطالب بنجاح", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("قبول واعتماد الحساب", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            AppDataRepository.rejectRegistration(req.id)
                                            Toast.makeText(context, "تم رفض الطلب", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("رفض الطلب", fontSize = 11.sp, color = CrimsonSecurity)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. الاشتراكات
        if (selectedSection == 3) {
            item {
                Text("إدارة الاشتراكات وطلبات السداد عبر المحافظ (782916997):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            items(AppDataRepository.paymentRequests) { pr ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(pr.studentName, fontWeight = FontWeight.Bold)
                            Text(pr.amount, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        }
                        Text("المحفظة: ${pr.walletType} | رقم الحوالة: ${pr.transferRef}", fontSize = 11.sp, color = NavyPrimary)
                        Text("رقم المحول: ${pr.senderNumber} | الحالة: ${pr.status}", fontSize = 11.sp)

                        if (pr.status == "PENDING") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        AppDataRepository.approvePayment(pr.id)
                                        Toast.makeText(context, "تم تفعيل الاشتراك", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("قبول وتفعيل", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = {
                                        AppDataRepository.rejectPayment(pr.id)
                                        Toast.makeText(context, "تم الرفض", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("رفض", fontSize = 11.sp, color = CrimsonSecurity)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. أكواد التفعيل
        if (selectedSection == 4) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إنشاء حزمة أكواد تفعيل جديدة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(1, 5, 10).forEach { cnt ->
                                FilterChip(
                                    selected = codeCountToGenerate == cnt,
                                    onClick = { codeCountToGenerate = cnt },
                                    label = { Text("$cnt أكواد") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(30 to "شهري", 90 to "فصلي", 180 to "نصف سنوي", 240 to "سنوي").forEach { (days, lbl) ->
                                FilterChip(
                                    selected = codeDaysSelection == days,
                                    onClick = { codeDaysSelection = days },
                                    label = { Text(lbl) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Button(
                            onClick = {
                                val generatedList = mutableListOf<String>()
                                repeat(codeCountToGenerate) {
                                    val codeStr = "$codeCustomPrefix-" + (1000..9999).random()
                                    AppDataRepository.subscriptionCodes.add(
                                        0,
                                        SubscriptionCode(
                                            id = System.currentTimeMillis() + it,
                                            code = codeStr,
                                            planName = "باقة $codeDaysSelection يوماً",
                                            durationDays = codeDaysSelection,
                                            isUsed = false
                                        )
                                    )
                                    generatedList.add(codeStr)
                                }
                                AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "توليد أكواد تفعيل", "تم إنشاء $codeCountToGenerate كود تفعيل للباقة $codeDaysSelection يوماً", "المالك"))
                                Toast.makeText(context, "تم توليد ${generatedList.size} أكواد تفعيل بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("توليد $codeCountToGenerate أكواد الآن")
                        }
                    }
                }
            }

            items(AppDataRepository.subscriptionCodes) { code ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(code.code, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = NavyPrimary)
                            Text("${code.planName} (${code.durationDays} يوماً)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (code.isUsed) {
                                Text("مستخدم بواسطة: ${code.usedByUserName ?: "طالب"}", fontSize = 10.sp, color = EmeraldSuccess)
                            }
                        }
                        Surface(
                            color = if (code.isUsed) MaterialTheme.colorScheme.surfaceVariant else EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (code.isUsed) "مستخدم" else "متاح",
                                color = if (code.isUsed) MaterialTheme.colorScheme.onSurfaceVariant else EmeraldSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6. الكتب والملازم
        if (selectedSection == 5) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إضافة كتاب أو ملزمة وزارية جديدة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(value = bookletTitle, onValueChange = { bookletTitle = it }, label = { Text("عنوان الملزمة") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("الرياضيات", "الفيزياء", "الكيمياء", "الأحياء", "اللغة الإنجليزية").forEach { s ->
                                FilterChip(selected = bookletSubject == s, onClick = { bookletSubject = s }, label = { Text(s, fontSize = 10.sp) })
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("PDF", "WORD").forEach { t ->
                                FilterChip(selected = bookletType == t, onClick = { bookletType = t }, label = { Text(t) })
                            }
                        }
                        Button(
                            onClick = {
                                if (bookletTitle.isNotBlank()) {
                                    AppDataRepository.booklets.add(
                                        Booklet(
                                            id = AppDataRepository.booklets.size + 1,
                                            title = bookletTitle.trim(),
                                            subjectName = bookletSubject,
                                            description = "ملزمة وزارية صادرة عن أكاديمية رقيب للثانوية العامة.",
                                            fileType = bookletType,
                                            fileName = "${bookletTitle.trim()}.${bookletType.lowercase()}",
                                            fileSize = "3.2 MB",
                                            isPublished = true,
                                            isFree = bookletIsFree
                                        )
                                    )
                                    bookletTitle = ""
                                    AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "إضافة ملزمة", "تم نشر ملزمة $bookletTitle", "المالك"))
                                    Toast.makeText(context, "تمت إضافة الملزمة بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("نشر الملزمة للطلاب")
                        }
                    }
                }
            }

            items(AppDataRepository.booklets) { b ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(b.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${b.subjectName} | ${b.fileType} | ${b.fileSize}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = {
                            AppDataRepository.booklets.remove(b)
                            Toast.makeText(context, "تم حذف الملزمة", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonSecurity)
                        }
                    }
                }
            }
        }

        // 7. النماذج والملخصات
        if (selectedSection == 6) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إضافة نموذج اختبار أو ملخص درس:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(value = summaryTitle, onValueChange = { summaryTitle = it }, label = { Text("عنوان النموذج أو الملخص") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("ملخص درس", "نموذج اختبار", "مراجعة نهائية", "ورقة عمل").forEach { t ->
                                FilterChip(selected = summaryType == t, onClick = { summaryType = t }, label = { Text(t, fontSize = 10.sp) })
                            }
                        }
                        Button(
                            onClick = {
                                if (summaryTitle.isNotBlank()) {
                                    AppDataRepository.summaries.add(
                                        SummaryReviewItem(
                                            id = System.currentTimeMillis(),
                                            title = summaryTitle.trim(),
                                            description = "محتوى تعليمي ومراجعة وزارية معتمدة من أكاديمية رقيب.",
                                            subjectName = summarySubject,
                                            type = summaryType
                                        )
                                    )
                                    summaryTitle = ""
                                    Toast.makeText(context, "تم حفظ ونشر المحتوى بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("حفظ ونشر النموذج")
                        }
                    }
                }
            }

            items(AppDataRepository.summaries) { s ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(s.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${s.subjectName} | ${s.type}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = {
                            AppDataRepository.summaries.remove(s)
                            Toast.makeText(context, "تم الحذف", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonSecurity)
                        }
                    }
                }
            }
        }

        // 8. الأسئلة والاختبارات
        if (selectedSection == 7) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إضافة سؤال وزاري جديد للبنك:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(value = qText, onValueChange = { qText = it }, label = { Text("نص السؤال") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("MULTIPLE_CHOICE" to "اختيار من متعدد", "TRUE_FALSE" to "صح أو خطأ").forEach { (type, lbl) ->
                                FilterChip(selected = qType == type, onClick = { qType = type }, label = { Text(lbl) })
                            }
                        }
                        if (qType == "MULTIPLE_CHOICE") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(value = qOptA, onValueChange = { qOptA = it }, label = { Text("خيار أ") }, modifier = Modifier.weight(1f))
                                OutlinedTextField(value = qOptB, onValueChange = { qOptB = it }, label = { Text("خيار ب") }, modifier = Modifier.weight(1f))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(value = qOptC, onValueChange = { qOptC = it }, label = { Text("خيار ج") }, modifier = Modifier.weight(1f))
                                OutlinedTextField(value = qOptD, onValueChange = { qOptD = it }, label = { Text("خيار د") }, modifier = Modifier.weight(1f))
                            }
                        }
                        OutlinedTextField(value = qExplanation, onValueChange = { qExplanation = it }, label = { Text("التفسير والتعليل الوزاري") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                        Button(
                            onClick = {
                                if (qText.isNotBlank()) {
                                    AppDataRepository.questions.add(
                                        Question(
                                            id = AppDataRepository.questions.size + 1,
                                            lessonId = 1,
                                            subjectId = qSubjectId,
                                            questionText = qText.trim(),
                                            type = qType,
                                            optionA = qOptA,
                                            optionB = qOptB,
                                            optionC = qOptC,
                                            optionD = qOptD,
                                            correctOption = qCorrect,
                                            explanation = qExplanation.trim()
                                        )
                                    )
                                    qText = ""
                                    qExplanation = ""
                                    Toast.makeText(context, "تمت إضافة السؤال بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("حفظ السؤال في بنك الأسئلة")
                        }
                    }
                }
            }
        }

        // 9. مركز الإشعارات
        if (selectedSection == 8) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("إرسال إشعار فوري للطلاب:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(value = notifTitle, onValueChange = { notifTitle = it }, label = { Text("عنوان الإشعار") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        OutlinedTextField(value = notifMessage, onValueChange = { notifMessage = it }, label = { Text("نص الرسالة أو التنبيه") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("إعلان عام", "محتوى جديد", "امتحان أو اختبار", "تنبيه إداري").forEach { t ->
                                FilterChip(selected = notifType == t, onClick = { notifType = t }, label = { Text(t, fontSize = 10.sp) })
                            }
                        }

                        Button(
                            onClick = {
                                if (notifTitle.isNotBlank() && notifMessage.isNotBlank()) {
                                    AppDataRepository.notifications.add(
                                        0,
                                        NotificationItem(
                                            id = System.currentTimeMillis(),
                                            title = notifTitle.trim(),
                                            message = notifMessage.trim(),
                                            type = notifType,
                                            targetAudience = notifAudience
                                        )
                                    )
                                    AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "إرسال إشعار", "تم بث الإشعار: $notifTitle للجمهور: $notifAudience", "المالك"))
                                    notifTitle = ""
                                    notifMessage = ""
                                    Toast.makeText(context, "تم إرسال الإشعار وحفظه بنجاح!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("إرسال الإشعار لجميع الطلاب")
                        }
                    }
                }
            }

            items(AppDataRepository.notifications) { n ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(n.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${n.type} | الجمهور: ${n.targetAudience}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = {
                            AppDataRepository.notifications.remove(n)
                            Toast.makeText(context, "تم حذف الإشعار", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonSecurity)
                        }
                    }
                }
            }
        }

        // 10. إدارة المحتوى
        if (selectedSection == 9) {
            item {
                Text("المواد الدراسية المقررة في المنصة:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            items(AppDataRepository.subjects) { subj ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(subj.iconEmoji, fontSize = 24.sp)
                            Column {
                                Text(subj.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(subj.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Surface(color = NavyPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                            Text(subj.track, color = NavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // 11. إعدادات التطبيق
        if (selectedSection == 10) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("إعدادات تطبيق رقيب العامة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("• اسم التطبيق: أكاديمية رقيب للتعليم الثانوي", fontSize = 12.sp)
                        Text("• شعار المنصة: افهم • حل • اختبر • تفوق", fontSize = 12.sp)
                        Text("• رقم الحساب الموحد للمحافظ اليمنية: ${AppDataRepository.YEMENI_WALLET_ACCOUNT}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                        Text("• هاتف الدعم الفني والمالك: ${AppDataRepository.SUPPORT_WHATSAPP}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text("• المحافظ المدعومة: جيب (Jaib)، جوالي (Jawali)، ون كاش (One Cash)، الكريمي (Kuraimi)", fontSize = 12.sp)
                    }
                }
            }
        }

        // 12. سجل العمليات
        if (selectedSection == 11) {
            item {
                Text("سجل الرقابة والعمليات الإدارية والأمنية:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            items(AppDataRepository.adminLogs) { log ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(log.action, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(sdf.format(Date(log.timestamp)), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(log.details, fontSize = 11.sp)
                        Text("المشرف: ${log.performedBy}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // 13. حساب المالك والخروج
        if (selectedSection == 12) {
            item {
                Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("بيانات أمان المالك (التحقق الرباعي):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("1. البريد الإلكتروني: ${AppDataRepository.OWNER_EMAIL}", fontSize = 12.sp)
                        Text("2. رقم هاتف المالك: ${AppDataRepository.OWNER_PHONE}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)

                        OutlinedTextField(
                            value = currentPasswordEdit,
                            onValueChange = { currentPasswordEdit = it },
                            label = { Text("3. كلمة المرور الأساسية للمالك") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = currentSecretEdit,
                            onValueChange = { currentSecretEdit = it },
                            label = { Text("4. كلمة المرور الإضافية السرية (الرمز السري)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                AppDataRepository.ownerPassword.value = currentPasswordEdit.trim()
                                AppDataRepository.ownerSecretKey.value = currentSecretEdit.trim()
                                AppDataRepository.adminLogs.add(0, AdminLog(System.currentTimeMillis(), "تعديل أمان المالك", "تم تحديث كلمة المرور وكلمة السر الأمنية", "المالك"))
                                Toast.makeText(context, "تم حفظ بيانات الأمان الجديدة بنجاح", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("تحديث بيانات الأمان")
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecurity),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تسجيل الخروج من لوحة المالك")
                        }
                    }
                }
            }
        }
    }
}
