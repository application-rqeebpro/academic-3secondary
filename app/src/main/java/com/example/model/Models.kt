package com.example.model

enum class Screen {
    HOME,
    SUBJECTS,
    UNITS,
    LESSON_DETAIL,
    SOLVE_MY_QUESTION,
    SMART_TUTOR,
    EXPLAIN_ME,
    EXAMS,
    EXAM_SESSION,
    EXAM_RESULT,
    QUESTION_BANK,
    BOOKLETS,
    CALCULATOR,
    SUBSCRIPTION,
    PAYMENT_FORM,
    ACTIVATE_CODE,
    PROFILE,
    LOGIN,
    REGISTER,
    SEARCH,
    FAVORITES,
    PROGRESS,
    NOTIFICATIONS,
    OWNER_LOGIN,
    OWNER_DASHBOARD
}

enum class UserRole {
    STUDENT,
    ADMIN
}

data class User(
    val id: Long,
    val fullName: String,
    val phoneOrEmail: String,
    val email: String = "",
    val universityOrSchool: String = "ثانوية المتفوقين",
    val passwordHash: String,
    val role: UserRole = UserRole.STUDENT,
    val grade: String = "الثالث الثانوي",
    val track: String = "علمي",
    val isActive: Boolean = true,
    val isPendingApproval: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class Subject(
    val id: Int,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val colorHex: Long,
    val track: String = "علمي"
)

data class CurriculumUnit(
    val id: Int,
    val subjectId: Int,
    val unitNumber: Int,
    val title: String,
    val description: String
)

data class Lesson(
    val id: Int,
    val unitId: Int,
    val subjectId: Int,
    val lessonNumber: Int,
    val title: String,
    val coreIdea: String,
    val simplifiedExplanation: String,
    val formulas: String = "",
    val symbolsExplanation: String = "",
    val keyPoints: String = "",
    val solvedExample: String = "",
    val practiceExercise: String = "",
    val easierExplanation: String = ""
)

data class Question(
    val id: Int,
    val lessonId: Int,
    val subjectId: Int,
    val questionText: String,
    val type: String, // "MULTIPLE_CHOICE" or "TRUE_FALSE" or "TEXT"
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    val correctOption: String, // 'A', 'B', 'C', 'D', 'TRUE', 'FALSE', or text
    val explanation: String = "",
    val difficulty: String = "MEDIUM"
)

data class Exam(
    val id: Int,
    val subjectId: Int,
    val title: String,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val totalScore: Int = 100,
    val isPublished: Boolean = true,
    val track: String = "علمي",
    val questionIds: List<Int> = emptyList()
)

data class ExamResult(
    val id: Long,
    val userId: Long,
    val examId: Int,
    val subjectId: Int,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val isPassed: Boolean,
    val takenAt: Long = System.currentTimeMillis(),
    val answers: Map<Int, String> = emptyMap()
)

data class Booklet(
    val id: Int,
    val title: String,
    val subjectName: String,
    val grade: String = "الثالث الثانوي",
    val description: String,
    val fileType: String, // "PDF" or "WORD"
    val fileName: String,
    val fileSize: String,
    val downloadCount: Int = 1420,
    val isPublished: Boolean = true,
    val isFree: Boolean = false,
    val category: String = "ملزمة وزارية",
    val priority: Int = 1
)

data class SummaryReviewItem(
    val id: Long,
    val title: String,
    val description: String,
    val subjectName: String,
    val grade: String = "الثالث الثانوي",
    val type: String, // "ملخص درس", "نموذج اختبار", "مراجعة نهائية", "ورقة عمل"
    val fileUrl: String = "",
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class Subscription(
    val id: Long,
    val userId: Long,
    val planName: String,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean = true,
    val paymentMethod: String,
    val paymentRef: String
)

data class PaymentRequest(
    val id: Long,
    val userId: Long,
    val studentName: String,
    val studentPhone: String,
    val walletType: String, // "جيب (Jaib)", "جوالي (Jawali)", "ون كاش (One Cash)", "الكريمي (Kuraimi)"
    val senderNumber: String,
    val transferRef: String,
    val amount: String,
    val planName: String,
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val requestDate: Long = System.currentTimeMillis()
)

data class RegistrationRequest(
    val id: Long,
    val fullName: String,
    val phone: String,
    val email: String,
    val passwordHash: String,
    val universityOrSchool: String,
    val grade: String,
    val track: String,
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val requestDate: Long = System.currentTimeMillis()
)

data class SubscriptionCode(
    val id: Long,
    val code: String,
    val planName: String,
    val durationDays: Int,
    val isUsed: Boolean = false,
    val usedByUserId: Long? = null,
    val usedByUserName: String? = null,
    val usedDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class AdminLog(
    val id: Long,
    val action: String,
    val details: String,
    val performedBy: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class NotificationItem(
    val id: Long,
    val title: String,
    val message: String,
    val type: String = "إعلان عام", // إعلان عام، محتوى جديد، كتاب أو ملزمة، امتحان، تنبيه إداري
    val targetAudience: String = "جميع الطلاب", // جميع الطلاب، طلاب علمي، طلاب أدبي، المشتركون فقط
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class QuestionSolvedItem(
    val questionNumber: Int,
    val questionTitle: String,
    val questionText: String,
    val questionType: String, // "MULTIPLE_CHOICE", "TRUE_FALSE", "CALCULATION"
    val selectedOptionLabel: String = "",
    val selectedOptionText: String = "",
    val isTrue: Boolean? = null,
    val correction: String = "",
    val finalAnswer: String = "",
    val givens: List<String> = emptyList(),
    val laws: List<String> = emptyList(),
    val substitutionSteps: List<String> = emptyList(),
    val calculationSteps: List<String> = emptyList(),
    val unitCheck: String = "",
    val easierExplanation: String = ""
)

data class EducationalSolution(
    val isExamSheet: Boolean = false,
    val subject: String,
    val questionUnderstanding: String,
    val givens: List<String> = emptyList(),
    val required: String = "",
    val laws: List<String> = emptyList(),
    val substitutionSteps: List<String> = emptyList(),
    val calculationSteps: List<String> = emptyList(),
    val unitCheck: String = "",
    val finalAnswer: String,
    val confidence: String = "عالية",
    val easierExplanation: String = "",
    val items: List<QuestionSolvedItem> = emptyList(),
    val verificationPoints: List<String> = emptyList()
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestedVideoTopic: String? = null,
    val diagramDescription: String? = null,
    val mathFormula: String? = null
)

data class UserProgress(
    val lessonId: Int,
    val subjectId: Int,
    val isCompleted: Boolean,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class FavoriteItem(
    val id: Long,
    val itemType: String, // "LESSON", "BOOKLET", "QUESTION"
    val itemId: Int,
    val title: String,
    val subtitle: String
)
