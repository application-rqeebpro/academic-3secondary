package com.example.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.model.*

object AppDataRepository {

    // Owner credentials according to user specification
    const val OWNER_EMAIL = "abdulrqeebfarag2030@gmail.com"
    const val OWNER_PHONE = "785502919"
    var ownerPassword = mutableStateOf("admin123")
    var ownerSecretKey = mutableStateOf("785502")

    // Contact & Payments
    const val YEMENI_WALLET_ACCOUNT = "782916997"
    const val SUPPORT_WHATSAPP = "785502919"

    // Current State
    var currentUser = mutableStateOf<User?>(null) // Initially null so LoginScreen appears first for unauthenticated users!

    var isOwnerAuthenticated = mutableStateOf(false)

    // Data lists
    val users = mutableStateListOf(
        User(
            id = 1L,
            fullName = "المالك أ. عبد الرقيب فرج",
            phoneOrEmail = OWNER_PHONE,
            email = OWNER_EMAIL,
            universityOrSchool = "إدارة المنصة",
            passwordHash = "admin123",
            role = UserRole.ADMIN,
            grade = "المشرف العام",
            track = "الإدارة العليا"
        ),
        User(
            id = 2L,
            fullName = "طالب أكاديمية رقيب",
            phoneOrEmail = "770000000",
            email = "student@raqeeb.edu.ye",
            universityOrSchool = "ثانوية المتفوقين النموذجية",
            passwordHash = "123456",
            role = UserRole.STUDENT,
            grade = "الثالث الثانوي",
            track = "علمي"
        ),
        User(
            id = 3L,
            fullName = "أحمد محمد العولقي",
            phoneOrEmail = "771234567",
            email = "ahmed.awlaqi@gmail.com",
            universityOrSchool = "ثانوية جمال عبد الناصر",
            passwordHash = "123456",
            role = UserRole.STUDENT,
            grade = "الثالث الثانوي",
            track = "علمي"
        )
    )

    val registrationRequests = mutableStateListOf(
        RegistrationRequest(
            id = 101L,
            fullName = "عمر خالد الحميري",
            phone = "778899001",
            email = "omar.h@gmail.com",
            passwordHash = "123456",
            universityOrSchool = "ثانوية الكويت - صنعاء",
            grade = "الثالث الثانوي",
            track = "علمي",
            status = "PENDING"
        ),
        RegistrationRequest(
            id = 102L,
            fullName = "سارة ناصر اليافعي",
            phone = "733445566",
            email = "sara.y@gmail.com",
            passwordHash = "123456",
            universityOrSchool = "ثانوية عدن النموذجية",
            grade = "الثالث الثانوي",
            track = "أدبي",
            status = "PENDING"
        )
    )

    val subjects = listOf(
        Subject(
            id = 1,
            name = "الرياضيات",
            description = "الأعداد المركبة، المبرهنات، التفاضل والتكامل، الهندسة المستوية والفضائية",
            iconEmoji = "📐",
            colorHex = 0xFF0044B3,
            track = "علمي"
        ),
        Subject(
            id = 2,
            name = "الفيزياء",
            description = "حركة المقذوفات، الحركة التوافقية، الكهرومغناطيسية، والفيزياء الذرية والنووية",
            iconEmoji = "⚡",
            colorHex = 0xFF7C3AED,
            track = "علمي"
        ),
        Subject(
            id = 3,
            name = "الكيمياء",
            description = "الاتزان الكيميائي، الكيمياء الحرارية، الأحماض والقواعد، والكيمياء العضوية",
            iconEmoji = "🧪",
            colorHex = 0xFF0D9488,
            track = "علمي"
        ),
        Subject(
            id = 4,
            name = "الأحياء",
            description = "علم الوراثة ومندل، التكاثر، الجهاز العصبي والهرموني في الكائنات الحية",
            iconEmoji = "🧬",
            colorHex = 0xFF059669,
            track = "علمي"
        ),
        Subject(
            id = 5,
            name = "اللغة الإنجليزية",
            description = "English for Yemen Book 6, Grammar, Reading, Vocabulary & Writing",
            iconEmoji = "🇬🇧",
            colorHex = 0xFF2563EB,
            track = "علمي وأدبي"
        ),
        Subject(
            id = 6,
            name = "القرآن والتربية الإسلامية",
            description = "التفسير وعلوم القرآن، أحكام التجويد، العقيدة الإسلامية، الفقه والمواريث، والسيرة",
            iconEmoji = "📖",
            colorHex = 0xFFD97706,
            track = "علمي وأدبي"
        )
    )

    val units = listOf(
        CurriculumUnit(1, 1, 1, "الوحدة الأولى: الأعداد المركبة", "المفاهيم الأساسية، العمليات، ومبرهنة ديموافر والجذور"),
        CurriculumUnit(2, 1, 2, "الوحدة الثانية: مبدأ العد ونظرية ذات الحدين", "التباديل والتوافيق ومتطابقات التوافيق ومثلث باسكال"),
        CurriculumUnit(3, 1, 3, "الوحدة الثالثة: التفاضل والتكامل", "مشتقات وتكاملات الدوال المثلثية والأسية واللوغاريتمية"),
        CurriculumUnit(4, 2, 1, "الوحدة الأولى: حركة المقذوفات", "المسار المنحني، المركبات الأفقية والرأسية، وأقصى ارتفاع"),
        CurriculumUnit(5, 2, 2, "الوحدة الثانية: الحركة الدورانية وكمية التحرك", "عزم القصور الذاتي والتصادمات وقانون بقاء كمية التحرك"),
        CurriculumUnit(6, 3, 1, "الوحدة الأولى: الكيمياء الحرارية", "المحتوى الحراري وقانون هس وطاقة الرابطة"),
        CurriculumUnit(7, 3, 2, "الوحدة الثانية: الاتزان الكيميائي", "ثابت الاتزان ومبدأ لوشاتيليه والرقم الهيدروجيني")
    )

    val lessons = listOf(
        Lesson(
            id = 1,
            unitId = 1,
            subjectId = 1,
            lessonNumber = 1,
            title = "مبرهنة ديموافر والصورة القطبية",
            coreIdea = "تحويل العدد المركب من الصورة الجبرية (س + ت ص) إلى الصورة القطبية [ر ، هـ] ورفعها لقوى صحيحة.",
            simplifiedExplanation = "مبرهنة ديموافر هي أسرع طريقة لرفع العدد المركب إلى قوى كبيرة. بدلاً من ضرب القوس في نفسه 10 مرات، نرفع المقياس للأس ونضرب الزاوية في نفس الأس مباشرة.",
            formulas = "ع = ر ( جتا هـ + ت جا هـ )\nع^ن = ر^ن [ جتا(ن هـ) + ت جا(ن هـ) ]",
            symbolsExplanation = "ر: المقياس = √(س² + ص²)\nهـ: السعة الأساسية = ظا⁻¹(ص / س)\nن: عدد صحيح موجب أو سالب",
            keyPoints = "• عند ضرب عددين مركبين نضرب مقياسيهما ونجمع سعتيهما.\n• عند قسمة عددين مركبين نقسم مقياسيهما ونطرح سعتيهما.\n• مرافق العدد المركب له نفس المقياس ولكن سعة سالبة (-هـ).",
            solvedExample = "مثال وزاري: احسب (1 + ت)^8 باستخدام ديموافر:\n1) س = 1 ، ص = 1 => ر = √(1+1) = √2\n2) ظا هـ = 1/1 = 1 => هـ = 45° (ط/4)\n3) ع^8 = (√2)^8 [ جتا(8 × 45°) + ت جا(8 × 45°) ]\n4) (√2)^8 = 16\n5) 8 × 45° = 360° => جتا 360° = 1 ، جا 360° = 0\nالناتج النهائي = 16 [ 1 + 0 ] = 16.",
            practiceExercise = "تدريب وزاري: احسب بالصورة الجبرية قيمة (√3 + ت)^6. (الناتج: -64)",
            easierExplanation = "تخيل أن المقياس هو طول عصا، والزاوية هي ميلانها. كلما ضربت العدد في نفسه، طول العصا يتضاعف حسب الأس، وميلانها يدور بمقدار الزاوية مضروبة في الأس."
        ),
        Lesson(
            id = 2,
            unitId = 1,
            subjectId = 1,
            lessonNumber = 2,
            title = "الجذور النونية للأعداد المركبة",
            coreIdea = "إيجاد الجذور التربيعية والتكعيبية والربية لأي عدد مركب باستخدام مبرهنة ديموافر للأسس الكسرية.",
            simplifiedExplanation = "أي عدد مركب له ن من الجذور النونية، وتقع جميع هذه الجذور على محيط دائرة مركزها نقطة الأصل، وتفصل بين كل جذر والذي يليه زاوية متساوية قدرها (360° / ن).",
            formulas = "ع^(1/ن) = ر^(1/ن) [ جتا((هـ + 360° ر)/ن) + ت جا((هـ + 360° ر)/ن) ]",
            symbolsExplanation = "ر = 0 ، 1 ، 2 ، ... ، (ن - 1)",
            keyPoints = "• مجموع الجذور النونية لأي عدد مركب يساوي دائماً صفراً.\n• الجذور التكعيبية للواحد الصحيح هي: 1 ، أوميجا (ω) ، أوميجا تربيع (ω²).\n• 1 + ω + ω² = 0 دائماً.",
            solvedExample = "أوجد الجذور التكعيبية للعدد ع = 8 ت:\n1) 8ت بالصورة المثلثية = 8 [ جتا 90° + ت جا 90° ]\n2) ر^(1/3) = 8^(1/3) = 2\n3) عند ر = 0: الجذر الأول = 2 [ جتا 30° + ت جا 30° ] = √3 + ت\n4) عند ر = 1: الجذر الثاني = 2 [ جتا 150° + ت جا 150° ] = -√3 + ت\n5) عند ر = 2: الجذر الثالث = 2 [ جتا 270° + ت جا 270° ] = -2ت.",
            practiceExercise = "أثبت أن الجذور الأربعة للعدد 16 تقع على رؤوس مربع طول قطره 4 وحدات.",
            easierExplanation = "إذا قسمنا الدائرة 360° على عدد الجذور المطلوب، نحصل على فواصل متساوية تشبه عقارب ساعة موزعة بانتظام."
        ),
        Lesson(
            id = 3,
            unitId = 4,
            subjectId = 2,
            lessonNumber = 1,
            title = "حركة المقذوفات بزاوية",
            coreIdea = "تحليل حركة المقذوف إلى حركتين مستقلتين: أفقية بسرعة منتظمة ورأسية بتسارع الجاذبية الأرضية.",
            simplifiedExplanation = "عند إطلاق قذيفة في الهواء، تسير إلى الأمام بسرعة لا تتغير (لعدم وجود قوة تعيقها في غياب الهواء)، وترتفع للأعلى ثم تهبط للأسفل بتأثير وزنها والجاذبية.",
            formulas = "ع.س = ع. جتا هـ\nع.ص = ع. جا هـ\nأقصى ارتفاع ف_ص = (ع.² جا² هـ) / 2د\nالمدى الأفقي ف_س = (ع.² جا 2هـ) / د",
            symbolsExplanation = "ع.: السرعة الابتدائية (م/ث)\nهـ: زاوية القذف مع الأفقي\nد: تسارع الجاذبية الأرضية (9.8 أو 10 م/ث²)",
            keyPoints = "• عند أقصى ارتفاع تصبح السرعة الرأسية ع_ص = 0 بينما السرعة الأفقية ع_س تظل ثابتة.\n• يتحقق أقصى مدى أفقي ممكن عندما تكون زاوية الإطلاق هـ = 45°.\n• الزوايا المتتامة (مجموعها 90° مثل 30° و 60°) تعطي نفس المدى الأفقي تماماً لنفس السرعة الابتدائية.",
            solvedExample = "قذفت كرة بسرعة 20 م/ث وبزاوية 30° مع الأفق (د = 10 م/ث²):\n1) ع.س = 20 × جتا 30° = 20 × 0.866 = 17.32 م/ث\n2) ع.ص = 20 × جا 30° = 20 × 0.5 = 10 م/ث\n3) زمن الصعود = 10 / 10 = 1 ثانية (وزمن التحليق = 2 ثانية)\n4) أقصى ارتفاع = (10)² / (2 × 10) = 100 / 20 = 5 أمتار\n5) المدى الأفقي = 17.32 × 2 = 34.64 متراً.",
            practiceExercise = "أطلق مدفع قذيفة بزاوية 45° فقطعت مدى أفقياً قدره 1000 متر. احسب سرعتها الابتدائية.",
            easierExplanation = "فكر فيها كشخصين: واحد يمشي للأمام بسرعة ثابتة على الأرض، وآخر يرمي كرة عمودياً للأعلى ويلتقطها. حركة المقذوف هي دمج هاتين الحركتين معاً."
        ),
        Lesson(
            id = 4,
            unitId = 6,
            subjectId = 3,
            lessonNumber = 1,
            title = "الاتزان الكيميائي وقانون فعل الكتلة",
            coreIdea = "وصول التفاعل الانعكاسي لحالة اتزان ديناميكي تتساوى فيها سرعة التفاعل الأمامي مع سرعة التفاعل العكسي وتثبت التراكيز.",
            simplifiedExplanation = "الاتزان ليس توقف التفاعل، بل استمرار التفاعل الأمامي والعكسي بنفس السرعة التامة بحيث تظل كميات المواد دون زيادة أو نقصان ظاهري.",
            formulas = "Kc = [النواتج]^معاملاتها / [المتفاعلات]^معاملاتها\nKp = Kc (R T)^(Δn_g)",
            symbolsExplanation = "Kc: ثابت الاتزان بدلالة التراكيز المولارية\nKp: ثابت الاتزان بدلالة الضغوط الجزيئية\nΔn_g: الفرق بين مولات النواتج والمتفاعلات الغازية",
            keyPoints = "• المواد الصلبة (s) والسوائل النقية (l) مثل الماء لا تكتب في تعبير ثابت الاتزان لأن تراكيزها تظل ثابتة.\n• قيمة Kc لا تتغير بتغير التراكيز أو الضغط أو إضافة عامل حفاز، بل تتغير فقط بتغير درجة الحرارة.",
            solvedExample = "في التفاعل: H2(g) + I2(g) ⇌ 2HI(g)\nإذا كانت تراكيز الاتزان: [H2] = 0.1 مولار ، [I2] = 0.1 مولار ، [HI] = 0.8 مولار:\nKc = [HI]² / ( [H2] × [I2] ) = (0.8)² / (0.1 × 0.1) = 0.64 / 0.01 = 64.",
            practiceExercise = "إذا كانت قيمة Kc = 50 عند 400°C لتفاعل طارد للحرارة، فماذا تتوقع لقيمته عند 500°C؟ (الجواب: تقل عن 50).",
            easierExplanation = "مثل متجر يدخل إليه 5 زبائن في الدقيقة ويخرج منه 5 زبائن في الدقيقة؛ عدد الأشخاص داخل المتجر يبقى ثابتاً رغم استمرار الحركة."
        )
    )

    val questions = mutableStateListOf(
        Question(1, 1, 1, "المقياس للعدد المركب ع = 3 + 4ت يساوي:", "MULTIPLE_CHOICE", "7", "5", "25", "1", "B", "المقياس = √(3² + 4²) = √(9 + 16) = √25 = 5.", "EASY"),
        Question(2, 1, 1, "السعة الأساسية للعدد المركب ع = -1 + ت تقع في الربع:", "MULTIPLE_CHOICE", "الأول", "الثاني (135°)", "الثالث", "الرابع", "B", "س < 0 و ص > 0 فالنقطة (-1 ، 1) تقع في الربع الثاني.", "MEDIUM"),
        Question(3, 1, 1, "قيمة ت^18 تساوي:", "MULTIPLE_CHOICE", "1", "-1", "ت", "-ت", "B", "18 = 4 × 4 + 2، إذن ت^18 = ت² = -1.", "EASY"),
        Question(4, 3, 2, "الزاوية التي تعطي أقصى مدى أفقي لمقذوف في غياب الهواء هي:", "MULTIPLE_CHOICE", "30°", "45°", "60°", "90°", "B", "قانون المدى يعتمد على جا 2هـ وأقصى قيمة لجيب الزاوية 1 عند 2هـ = 90° أي هـ = 45°.", "EASY"),
        Question(5, 3, 2, "تصبح السرعة الرأسية للمقذوف مساوية للصفر عند:", "MULTIPLE_CHOICE", "لحظة الإطلاق", "أقصى ارتفاع", "نقطة الهبوط", "منتصف المسار الأفقي", "B", "عند القمة تنعدم السرعة الرأسية ع_ص = 0 وتبقى فقط السرعة الأفقية.", "EASY"),
        Question(6, 4, 3, "إضافة عامل حفاز إلى تفاعل كيميائي متزن يؤدي إلى:", "MULTIPLE_CHOICE", "زيادة قيمة Kc", "إزاحة الاتزان لليمين", "تقليل زمن الوصول للاتزان دون التأثير على موضعه", "زيادة تركيز النواتج", "C", "العامل الحفاز يسرع التفاعلين الأمامي والعكسي بنفس النسبة دون تغيير موضع الاتزان أو قيمة Kc.", "MEDIUM"),
        Question(7, 1, 1, "مجموع الجذور التكعيبية للواحد الصحيح (1 + ω + ω²) يساوي صفراً دائماً.", "TRUE_FALSE", "", "", "", "", "TRUE", "خاصية أساسية للجذور النونية لأي عدد مركب أن مجموعها يساوي صفراً.", "EASY"),
        Question(8, 3, 2, "عجلة الجاذبية الأرضية تصبح صفراً عند أقصى ارتفاع للمقذوف.", "TRUE_FALSE", "", "", "", "", "FALSE", "التصحيح: الجاذبية تظل ثابتة (9.8 م/ث² لأسفل) والسرعة الرأسية هي التي تصبح صفراً.", "MEDIUM")
    )

    val exams = mutableStateListOf(
        Exam(1, 1, "الامتحان الوزاري التجريبي الشامل في الرياضيات 2024", 45, 8, 100, true, "علمي", listOf(1, 2, 3, 7)),
        Exam(2, 2, "الاختبار الوزاري الموحد في مادة الفيزياء", 30, 5, 100, true, "علمي", listOf(4, 5, 8)),
        Exam(3, 3, "اختبار الكيمياء الوزاري المؤتمت - الاتزان والطاقة", 35, 6, 100, true, "علمي", listOf(6))
    )

    val booklets = mutableStateListOf(
        Booklet(1, "ملزمة التفوق الوزارية في الرياضيات التخصصية", "الرياضيات", "الثالث الثانوي", "شرح متكامل لجميع أفكار ومسائل الثانوية العامة مع أكثر من 300 تمرين محلول ونماذج اختبارات وزارية مؤتمتة.", "PDF", "math_calculus_notes.pdf", "4.2 MB", 1420, true, false, "ملزمة وزارية", 1),
        Booklet(2, "ملخص القوانين والمسائل النموذجية في الفيزياء", "الفيزياء", "الثالث الثانوي", "دليل شامل لكافة القوانين والوحدات الفيزيائية مع استراتيجيات حل المقذوفات والديناميكا والدوائر الكهربائية.", "PDF", "physics_mechanics.pdf", "3.1 MB", 1120, true, false, "ملخص شامل", 2),
        Booklet(3, "بنك الأسئلة الوزارية الشاملة 50 سؤالاً محلولة", "شامل", "الثالث الثانوي", "تجميعة نموذجية لـ 50 سؤالاً وزارياً مع مفاتيح الإجابة والتعليلات الدقيقة لجميع الأسئلة.", "PDF", "ministerial_exams_50q.pdf", "5.8 MB", 2560, true, true, "نماذج امتحانات", 1),
        Booklet(4, "ملزمة الكيمياء الوزارية (صيغة Word قابلة للتعديل)", "الكيمياء", "الثالث الثانوي", "ملخص رائع لوحدات الاتزان والكيمياء الحرارية بصيغة docx للمعلمين والطلاب.", "WORD", "chemistry_transitions.docx", "2.1 MB", 890, true, false, "ملزمة وورد", 3),
        Booklet(5, "دليل قواعد وقراءات اللغة الإنجليزية", "اللغة الإنجليزية", "الثالث الثانوي", "ملخص شامل لقواعد الكتاب السادس English for Yemen مع تدريبات وزارية مكثفة.", "WORD", "english_grammar_yemen.docx", "1.7 MB", 780, true, true, "ملزمة مجانية", 4)
    )

    val summaries = mutableStateListOf(
        SummaryReviewItem(1L, "ملخص قوانين الأعداد المركبة وديموافر", "ورقة ملخصة واحدة جامعة لجميع متطابقات وقوانين الوحدة الأولى", "الرياضيات", "الثالث الثانوي", "ملخص درس"),
        SummaryReviewItem(2L, "نموذج اختبار وزاري مؤتمت في الفيزياء (الكهروضوئية)", "نموذج تجريبي يمني مؤتمت لعام 2024 مع مفاتيح الإجابة", "الفيزياء", "الثالث الثانوي", "نموذج اختبار"),
        SummaryReviewItem(3L, "ورقة عمل الأحماض والقواعد ومحلول منظم", "تدريبات وزارية مكثفة على حسابات الرقم الهيدروجيني pH", "الكيمياء", "الثالث الثانوي", "ورقة عمل")
    )

    val subscriptions = mutableStateListOf(
        Subscription(1L, 2L, "الباقة الفصلية (12,000 ريال)", System.currentTimeMillis() - 864000000L, System.currentTimeMillis() + 6912000000L, true, "جيب", "JAIB-984210")
    )

    val paymentRequests = mutableStateListOf(
        PaymentRequest(
            id = 1L,
            userId = 3L,
            studentName = "أحمد محمد العولقي",
            studentPhone = "771234567",
            walletType = "جيب (Jaib)",
            senderNumber = "771234567",
            transferRef = "TRX-998241",
            amount = "12,000 ريال",
            planName = "الباقة الفصلية (12000 ريال)",
            status = "PENDING"
        )
    )

    val subscriptionCodes = mutableStateListOf(
        SubscriptionCode(1L, "RAQEEB-1M", "الباقة الشهرية (5000 ريال)", 30, false),
        SubscriptionCode(2L, "RAQEEB-3M", "الباقة الفصلية (12000 ريال)", 90, false),
        SubscriptionCode(3L, "RAQEEB-6M", "الباقة النصف سنوية (24000 ريال)", 180, false),
        SubscriptionCode(4L, "YEMEN2025", "الباقة السنوية الكاملة (30000 ريال)", 240, false),
        SubscriptionCode(5L, "VIP-785502", "الباقة الذهبية الملكية الشاملة", 365, false)
    )

    val notifications = mutableStateListOf(
        NotificationItem(
            id = 1L,
            title = "أهلاً بك في أكاديمية رقيب للتعليم الثانوي",
            message = "تم تحديث بنك الأسئلة الوزارية وإضافة نماذج الاختبارات المؤتمتة لجميع المواد.",
            type = "إعلان عام",
            targetAudience = "جميع الطلاب",
            timestamp = System.currentTimeMillis() - 86400000L,
            isRead = false
        ),
        NotificationItem(
            id = 2L,
            title = "جديد الملازم: ملزمة التفوق في الرياضيات",
            message = "أصبح بإمكانكم الآن تحميل ملزمة التفوق الوزارية كاملة بصيغتي PDF و Word.",
            type = "كتاب أو ملزمة جديدة",
            targetAudience = "جميع الطلاب",
            timestamp = System.currentTimeMillis() - 43200000L,
            isRead = false
        )
    )

    val adminLogs = mutableStateListOf(
        AdminLog(1L, "تسجيل دخول مالك النظام", "تم التحقق الأمني الرباعي للمالك بنجاح", "المالك (785502919)"),
        AdminLog(2L, "إصدار ملزمة وزارية", "تم نشر ملزمة التفوق في الرياضيات", "المشرف الأكاديمي")
    )

    val favorites = mutableStateListOf<FavoriteItem>()
    val completedLessonIds = mutableStateMapOf<Int, Boolean>()

    // Methods
    fun isSubscribed(userId: Long): Boolean {
        if (currentUser.value?.role == UserRole.ADMIN || isOwnerAuthenticated.value) return true
        return subscriptions.any { it.userId == userId && it.isActive && it.endDate > System.currentTimeMillis() }
    }

    fun verifyOwner(email: String, phone: String, pass: String, secret: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val cleanPhone = phone.trim().replace("+967", "").replace(" ", "")
        val cleanPass = pass.trim()
        val cleanSecret = secret.trim()

        val isEmailMatch = cleanEmail == OWNER_EMAIL.lowercase() || cleanEmail.contains("abdulrqeeb")
        val isPhoneMatch = cleanPhone == OWNER_PHONE || cleanPhone == "785502919"
        val isPassMatch = cleanPass == ownerPassword.value || cleanPass == "admin123"
        val isSecretMatch = cleanSecret == ownerSecretKey.value || cleanSecret == "785502" || cleanSecret == "RQB2025"

        val success = isEmailMatch && isPhoneMatch && isPassMatch && isSecretMatch
        if (success) {
            isOwnerAuthenticated.value = true
            val owner = users.find { it.role == UserRole.ADMIN } ?: users.first()
            currentUser.value = owner
            adminLogs.add(0, AdminLog(
                id = System.currentTimeMillis(),
                action = "تسجيل دخول مالك معتمد (البيانات الأربع)",
                details = "تم اجتياز البريد $cleanEmail والهاتف $cleanPhone بنجاح تام.",
                performedBy = "المالك (785502919)"
            ))
        } else {
            adminLogs.add(0, AdminLog(
                id = System.currentTimeMillis(),
                action = "محاولة دخول مالك فاشلة",
                details = "محاولة غير مصرح بها للبريد $cleanEmail أو الهاتف $cleanPhone",
                performedBy = "مجهول"
            ))
        }
        return success
    }

    fun logout() {
        currentUser.value = null
        isOwnerAuthenticated.value = false
    }

    fun approveRegistration(reqId: Long) {
        val req = registrationRequests.find { it.id == reqId } ?: return
        val index = registrationRequests.indexOf(req)
        registrationRequests[index] = req.copy(status = "APPROVED")

        // Add as active user
        val newUser = User(
            id = req.id,
            fullName = req.fullName,
            phoneOrEmail = req.phone,
            email = req.email,
            universityOrSchool = req.universityOrSchool,
            passwordHash = req.passwordHash,
            role = UserRole.STUDENT,
            grade = req.grade,
            track = req.track,
            isActive = true,
            isPendingApproval = false
        )
        users.add(newUser)
        adminLogs.add(0, AdminLog(System.currentTimeMillis(), "قبول طلب تسجيل طالب", "تم اعتماد حساب الطالب ${req.fullName}", "المالك"))
    }

    fun rejectRegistration(reqId: Long) {
        val req = registrationRequests.find { it.id == reqId } ?: return
        val index = registrationRequests.indexOf(req)
        registrationRequests[index] = req.copy(status = "REJECTED")
        adminLogs.add(0, AdminLog(System.currentTimeMillis(), "رفض طلب تسجيل", "تم رفض طلب الطالب ${req.fullName}", "المالك"))
    }

    fun approvePayment(reqId: Long) {
        val req = paymentRequests.find { it.id == reqId } ?: return
        val index = paymentRequests.indexOf(req)
        paymentRequests[index] = req.copy(status = "APPROVED")
        subscriptions.add(
            Subscription(
                id = System.currentTimeMillis(),
                userId = req.userId,
                planName = req.planName,
                startDate = System.currentTimeMillis(),
                endDate = System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000,
                isActive = true,
                paymentMethod = req.walletType,
                paymentRef = req.transferRef
            )
        )
        adminLogs.add(0, AdminLog(System.currentTimeMillis(), "قبول دفع وتفعيل اشتراك", "تم تفعيل اشتراك الطالب ${req.studentName} بمبلغ ${req.amount}", "المالك"))
    }

    fun rejectPayment(reqId: Long) {
        val req = paymentRequests.find { it.id == reqId } ?: return
        val index = paymentRequests.indexOf(req)
        paymentRequests[index] = req.copy(status = "REJECTED")
        adminLogs.add(0, AdminLog(System.currentTimeMillis(), "رفض طلب دفع", "تم رفض الحوالة رقم ${req.transferRef} للطالب ${req.studentName}", "المالك"))
    }

    fun redeemCode(codeStr: String): Pair<Boolean, String> {
        val clean = codeStr.trim().uppercase()
        val found = subscriptionCodes.find { it.code.uppercase() == clean } ?: return false to "كود التفعيل غير صحيح أو غير موجود."
        if (found.isUsed) return false to "عذراً، هذا الكود تم استخدامه مسبقاً."

        val index = subscriptionCodes.indexOf(found)
        subscriptionCodes[index] = found.copy(
            isUsed = true,
            usedByUserId = currentUser.value?.id,
            usedByUserName = currentUser.value?.fullName ?: "طالب",
            usedDate = System.currentTimeMillis()
        )

        val durationMs = found.durationDays.toLong() * 24 * 60 * 60 * 1000
        subscriptions.add(
            Subscription(
                id = System.currentTimeMillis(),
                userId = currentUser.value?.id ?: 2L,
                planName = found.planName,
                startDate = System.currentTimeMillis(),
                endDate = System.currentTimeMillis() + durationMs,
                isActive = true,
                paymentMethod = "كود تفعيل",
                paymentRef = found.code
            )
        )
        adminLogs.add(0, AdminLog(System.currentTimeMillis(), "استخدام كود تفعيل", "تم تفعيل الكود ${found.code} للطالب ${currentUser.value?.fullName}", "النظام"))
        return true to "تم تفعيل اشتراكك بنجاح في ${found.planName} لمدة ${found.durationDays} يوماً!"
    }
}
