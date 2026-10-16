package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.calculator.CalculatorEngine
import com.example.data.AppDataRepository
import com.example.model.ChatMessage
import com.example.model.PaymentRequest
import com.example.model.Screen
import com.example.ui.theme.*

@Composable
fun ScientificCalculatorScreen() {
    var activeSubTab by remember { mutableStateOf(0) }
    val calcTabs = listOf("الحاسبة العلمية", "الأعداد المركبة", "المصفوفات والمعادلات")

    // Standard Calc State
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var angleMode by remember { mutableStateOf(CalculatorEngine.AngleMode.DEGREE) }

    // Complex State
    var compReal by remember { mutableStateOf("3") }
    var compImag by remember { mutableStateOf("4") }
    var compPower by remember { mutableStateOf("2") }

    // Matrices / Equations State
    var eqA by remember { mutableStateOf("1") }
    var eqB by remember { mutableStateOf("-5") }
    var eqC by remember { mutableStateOf("6") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("calculator_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ScrollableTabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = NavyPrimary,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                calcTabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = activeSubTab == idx,
                        onClick = { activeSubTab = idx },
                        text = { Text(title, fontSize = 12.sp, fontWeight = if (activeSubTab == idx) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        // TAB 0: SCIENTIFIC
        if (activeSubTab == 0) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = angleMode == CalculatorEngine.AngleMode.DEGREE,
                                onClick = {
                                    angleMode = if (angleMode == CalculatorEngine.AngleMode.DEGREE) CalculatorEngine.AngleMode.RADIAN else CalculatorEngine.AngleMode.DEGREE
                                },
                                label = { Text(if (angleMode == CalculatorEngine.AngleMode.DEGREE) "درجات (DEG)" else "راديان (RAD)") }
                            )
                            Text(
                                text = expression.ifEmpty { "0" },
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = resultText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = NavyPrimary
                        )
                    }
                }
            }

            // Keypad
            item {
                val buttons = listOf(
                    listOf("C", "DEL", "(", ")"),
                    listOf("sin", "cos", "tan", "÷"),
                    listOf("7", "8", "9", "×"),
                    listOf("4", "5", "6", "−"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", ".", "^", "=")
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    buttons.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { btn ->
                                Button(
                                    onClick = {
                                        when (btn) {
                                            "C" -> {
                                                expression = ""
                                                resultText = "0"
                                            }
                                            "DEL" -> {
                                                if (expression.isNotEmpty()) expression = expression.dropLast(1)
                                            }
                                            "=" -> {
                                                resultText = CalculatorEngine.evaluateExpression(expression, angleMode)
                                            }
                                            else -> {
                                                expression += btn
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when (btn) {
                                            "=" -> NavyPrimary
                                            "C", "DEL" -> CrimsonSecurity.copy(alpha = 0.15f)
                                            "+", "−", "×", "÷", "^" -> GoldAccent.copy(alpha = 0.2f)
                                            "sin", "cos", "tan", "(", ")" -> NavyPrimary.copy(alpha = 0.1f)
                                            else -> MaterialTheme.colorScheme.surface
                                        },
                                        contentColor = when (btn) {
                                            "=" -> Color.White
                                            "C", "DEL" -> CrimsonSecurity
                                            "+", "−", "×", "÷", "^" -> AmberWarning
                                            "sin", "cos", "tan", "(", ")" -> NavyPrimary
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                                ) {
                                    Text(btn, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: COMPLEX NUMBERS
        if (activeSubTab == 1) {
            val rVal = compReal.toDoubleOrNull() ?: 0.0
            val iVal = compImag.toDoubleOrNull() ?: 0.0
            val cNum = CalculatorEngine.ComplexNumber(rVal, iVal)
            val pwr = compPower.toIntOrNull() ?: 1
            val powered = cNum.power(pwr)

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("العدد المركب: ع = س + ت ص", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = compReal,
                                onValueChange = { compReal = it },
                                label = { Text("س (الحقيقي)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = compImag,
                                onValueChange = { compImag = it },
                                label = { Text("ص (التخيلي)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Text("• الصورة الجبرية: ع = ${cNum.toCartesianString()}", fontWeight = FontWeight.Bold, color = NavyPrimary)
                        Text("• المقياس: ر = |ع| = " + String.format("%.3f", cNum.modulus), fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text("• السعة بالدرجات: هـ = " + String.format("%.2f", cNum.argumentDeg) + "°", fontWeight = FontWeight.Bold, color = AmberWarning)
                        Text("• السعة بالراديان: هـ = " + String.format("%.3f", cNum.argumentRad) + " rad", color = MaterialTheme.colorScheme.onSurface)
                        Text("• الصورة القطبية: ${cNum.toPolarString()}", color = PurpleAccent, fontWeight = FontWeight.Bold)

                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("رفع الأس (ديموافر ع^ن):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = compPower,
                                onValueChange = { compPower = it },
                                label = { Text("الأس ن") },
                                modifier = Modifier.width(90.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        Text("الناتج ع^$pwr = ${powered.toCartesianString()}", fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                }
            }
        }

        // TAB 2: EQUATIONS
        if (activeSubTab == 2) {
            val a = eqA.toDoubleOrNull() ?: 1.0
            val b = eqB.toDoubleOrNull() ?: -5.0
            val c = eqC.toDoubleOrNull() ?: 6.0
            val (roots, explanation) = CalculatorEngine.solveQuadratic(a, b, c)

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("حل المعادلة من الدرجة الثانية (أ س² + ب س + جـ = 0):", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = eqA, onValueChange = { eqA = it }, label = { Text("أ") }, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = eqB, onValueChange = { eqB = it }, label = { Text("ب") }, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = eqC, onValueChange = { eqC = it }, label = { Text("جـ") }, modifier = Modifier.weight(1f))
                        }

                        Text("التوضيح: $explanation", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        roots.forEach { root ->
                            Text(root, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookletsScreen() {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("الكل") }

    val filtered = AppDataRepository.booklets.filter {
        selectedFilter == "الكل" || it.fileType == selectedFilter
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("booklets_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("الملازم والملخصات الوزارية الرسمية", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("ملازم المنهج اليمني ونماذج الأسئلة الوزارية بصيغ PDF و Word متاحة للتحميل والمذاكرة.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("الكل", "PDF", "WORD").forEach { f ->
                    FilterChip(
                        selected = selectedFilter == f,
                        onClick = { selectedFilter = f },
                        label = { Text(f) }
                    )
                }
            }
        }

        items(filtered) { booklet ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = if (booklet.fileType == "PDF") CrimsonSecurity.copy(alpha = 0.15f) else NavyPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = booklet.fileType,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (booklet.fileType == "PDF") CrimsonSecurity else NavyPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Column {
                            Text(booklet.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${booklet.subjectName} | الحجم: ${booklet.fileSize}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(booklet.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }

                    IconButton(onClick = {
                        Toast.makeText(context, "بدأ تنزيل ملزمة: ${booklet.title}", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Download, contentDescription = "تحميل", tint = NavyPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionScreen(
    onNavigate: (Screen) -> Unit
) {
    val plans = listOf(
        Triple("الباقة الشهرية", "5,000 ريال يمني", 30 to "شاملة حل المسائل وبنك الأسئلة لشهر كامل"),
        Triple("الباقة الفصلية (الأكثر طلباً ⭐)", "12,000 ريال يمني", 90 to "توفير 3,000 ريال مع الملازم والامتحانات المؤتمتة"),
        Triple("الباقة النصف سنوية", "24,000 ريال يمني", 180 to "تغطية الفصلين الدراسيين الأول والثاني بالتفصيل"),
        Triple("الباقة السنوية الكاملة", "30,000 ريال يمني", 240 to "شاملة حتى نهاية الامتحانات الوزارية")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("subscription_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("باقات الاشتراك في أكاديمية رقيب", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("اختر الباقة المناسبة وسدد عبر المحافظ اليمنية أو أدخل كود التفعيل الفوري.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "حساب المحافظ الموحد: ${AppDataRepository.YEMENI_WALLET_ACCOUNT}",
                            color = GoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        TextButton(onClick = { onNavigate(Screen.ACTIVATE_CODE) }) {
                            Text("لديك كود تفعيل؟", color = GoldLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        items(plans) { (title, price, details) ->
            val (days, desc) = details
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
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NavyPrimary)
                        Text(price, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = EmeraldSuccess)
                    }

                    Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Button(
                        onClick = { onNavigate(Screen.PAYMENT_FORM) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("الاشتراك عبر المحافظ اليمنية", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentFormScreen(
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    var selectedWallet by remember { mutableStateOf("جيب (Jaib)") }
    var senderPhone by remember { mutableStateOf(AppDataRepository.currentUser.value?.phoneOrEmail ?: "") }
    var transferRef by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("12,000 ريال") }

    val wallets = listOf("جيب (Jaib)", "جوالي (Jawali)", "ون كاش (One Cash)", "الكريمي (Kuraimi)")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("payment_form_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("إرسال إشعار السداد", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("حول المبلغ إلى الحساب الموحد: ${AppDataRepository.YEMENI_WALLET_ACCOUNT} ثم سجل بيانات الحوالة أدناه للتفعيل.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("اختر المحفظة التي قمت بالتحويل منها:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        wallets.forEach { w ->
                            FilterChip(
                                selected = selectedWallet == w,
                                onClick = { selectedWallet = w },
                                label = { Text(w.split(" ")[0], fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = senderPhone,
                        onValueChange = { senderPhone = it },
                        label = { Text("رقم هاتف المحول") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = transferRef,
                        onValueChange = { transferRef = it },
                        label = { Text("رقم الحوالة أو السند (Reference No)") },
                        placeholder = { Text("مثال: TRX-998412") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("المبلغ المحول") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Button(
                        onClick = {
                            if (transferRef.isBlank() || senderPhone.isBlank()) {
                                Toast.makeText(context, "يرجى كتابة رقم الحوالة ورقم المحول", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val newReq = PaymentRequest(
                                id = System.currentTimeMillis(),
                                userId = AppDataRepository.currentUser.value?.id ?: 2L,
                                studentName = AppDataRepository.currentUser.value?.fullName ?: "طالب",
                                studentPhone = senderPhone.trim(),
                                walletType = selectedWallet,
                                senderNumber = senderPhone.trim(),
                                transferRef = transferRef.trim(),
                                amount = amount.trim(),
                                planName = "الباقة المختارة ($amount)"
                            )
                            AppDataRepository.paymentRequests.add(0, newReq)
                            Toast.makeText(context, "تم إرسال إشعار السداد للمالك بنجاح، سيتم التفعيل قريباً!", Toast.LENGTH_LONG).show()
                            onNavigate(Screen.HOME)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("إرسال إشعار التحويل", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivateCodeScreen(
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    var codeInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("activate_code_screen"),
        contentPadding = PaddingValues(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Icon(Icons.Default.Key, contentDescription = null, tint = PurpleAccent, modifier = Modifier.size(54.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("تفعيل كود الاشتراك الفوري", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("أدخل كود الاشتراك الصادر عن المالك لتفعيل حسابك فورياً.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { codeInput = it.uppercase() },
                        label = { Text("رمز الكود (مثال: RAQEEB-1M أو YEMEN2025)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Button(
                        onClick = {
                            val (success, message) = AppDataRepository.redeemCode(codeInput)
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (success) onNavigate(Screen.HOME)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("تفعيل الاشتراك الآن", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SmartTutorScreen() {
    var chatInput by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("1", false, "مرحباً بك في منصة المعلم الذكي لأكاديمية رقيب 🎓\nأنا جاهز للإجابة على أي سؤال أو مسألة في كافة مواد الثانوية العامة اليمنية مع توضيح القوانين والرسومات والمفاهيم.")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("smart_tutor_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.isUser) NavyPrimary else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                color = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = { chatInput = it },
                placeholder = { Text("اسأل المعلم الذكي...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            )

            IconButton(
                onClick = {
                    if (chatInput.isNotBlank()) {
                        val text = chatInput.trim()
                        messages.add(ChatMessage(System.currentTimeMillis().toString(), true, text))
                        chatInput = ""
                        // Intelligent curriculum answer response
                        val solution = com.example.solver.EducationalSolver.solve(text)
                        messages.add(
                            ChatMessage(
                                id = (System.currentTimeMillis() + 1).toString(),
                                isUser = false,
                                text = "إجابة المعلم الذكي:\n\n${solution.questionUnderstanding}\n\nالقانون: ${solution.laws.firstOrNull() ?: "المفهوم الوزاري المعتمد"}\n\nالنتيجة النهائية: ${solution.finalAnswer}\n\nالشرح المبسط: ${solution.easierExplanation}"
                            )
                        )
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(NavyPrimary)
            ) {
                Icon(Icons.Default.Send, contentDescription = "إرسال", tint = Color.White)
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    onSecretOwnerTrigger: () -> Unit
) {
    val currentUser = AppDataRepository.currentUser.value
    val isSub = AppDataRepository.isSubscribed(currentUser?.id ?: 2L)
    var versionTapCount by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text((currentUser?.fullName?.firstOrNull() ?: 'ط').toString(), color = NavyDeep, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                    }
                    Column {
                        Text(currentUser?.fullName ?: "طالب", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("الهاتف: ${currentUser?.phoneOrEmail}", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        Text("المدرسة/الجامعة: ${currentUser?.universityOrSchool ?: "ثانوية عامة"}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        Text("الحالة: " + if (isSub) "مشترك فعال ✔" else "بانتظار الاشتراك", color = if (isSub) EmeraldLight else GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("باقات الاشتراك والتفعيل", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Button(
                        onClick = { onNavigate(Screen.SUBSCRIPTION) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ترقية أو تجديد الاشتراك")
                    }
                    OutlinedButton(
                        onClick = { onNavigate(Screen.ACTIVATE_CODE) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("إدخال كود تفعيل")
                    }
                }
            }
        }

        // Student actions: Notifications and Logout
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigate(Screen.NOTIFICATIONS) }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = NavyPrimary)
                        Text("مركز الإشعارات والتنبيهات", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onLogout() }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = CrimsonSecurity)
                        Text("تسجيل الخروج من الحساب", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = CrimsonSecurity)
                    }
                }
            }
        }

        // Subtle app version footer (5 taps triggers secret owner login, no student visible hint)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable {
                        versionTapCount++
                        if (versionTapCount >= 5) {
                            versionTapCount = 0
                            onSecretOwnerTrigger()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "تطبيق رقيب للتعليم الثانوي — الإصدار 1.0.0",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}
