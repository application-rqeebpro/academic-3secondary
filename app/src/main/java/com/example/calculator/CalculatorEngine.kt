package com.example.calculator

import kotlin.math.*

object CalculatorEngine {

    enum class AngleMode { DEGREE, RADIAN }

    data class ComplexNumber(val real: Double, val imag: Double) {
        val modulus: Double get() = sqrt(real * real + imag * imag)

        val argumentDeg: Double
            get() {
                val rad = atan2(imag, real)
                val deg = Math.toDegrees(rad)
                return if (deg < 0) deg + 360.0 else deg
            }

        val argumentRad: Double
            get() {
                val rad = atan2(imag, real)
                return if (rad < 0) rad + 2 * Math.PI else rad
            }

        fun power(n: Int): ComplexNumber {
            val rN = modulus.pow(n.toDouble())
            val thetaN = argumentRad * n
            return ComplexNumber(rN * cos(thetaN), rN * sin(thetaN))
        }

        fun toCartesianString(): String {
            val r = if (abs(real) < 1e-5) "" else String.format("%.2f", real).trimEnd('0').trimEnd('.')
            val iVal = abs(imag)
            val i = if (iVal < 1e-5) "" else if (abs(iVal - 1.0) < 1e-5) "ت" else String.format("%.2f", iVal).trimEnd('0').trimEnd('.') + "ت"
            return when {
                r.isEmpty() && i.isEmpty() -> "0"
                r.isEmpty() -> if (imag < 0) "-$i" else i
                i.isEmpty() -> r
                else -> if (imag < 0) "$r - $i" else "$r + $i"
            }
        }

        fun toPolarString(): String {
            val rStr = String.format("%.2f", modulus).trimEnd('0').trimEnd('.')
            val degStr = String.format("%.1f", argumentDeg).trimEnd('0').trimEnd('.')
            return "[$rStr ، $degStr°] = $rStr ( جتا $degStr° + ت جا $degStr° )"
        }
    }

    fun evaluateExpression(expr: String, mode: AngleMode = AngleMode.DEGREE): String {
        val clean = expr.trim()
        if (clean.isEmpty()) return "0"
        return try {
            val sanitized = clean
                .replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")
                .replace("π", Math.PI.toString())
                .replace("e", Math.E.toString())

            val result = parseSimple(sanitized, mode)
            if (result.isNaN() || result.isInfinite()) {
                "خطأ رياضي"
            } else {
                String.format("%.4f", result).trimEnd('0').trimEnd('.')
            }
        } catch (e: Exception) {
            "خطأ"
        }
    }

    private fun parseSimple(str: String, mode: AngleMode): Double {
        var pos = -1
        var ch = ' '

        fun nextChar() {
            pos++
            ch = if (pos < str.length) str[pos] else '\u0000'
        }

        fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parseFactor(): Double {
            if (eat('+')) return parseFactor()
            if (eat('-')) return -parseFactor()

            var x: Double
            val startPos = pos
            if (eat('(')) {
                x = parseFactor()
                while (true) {
                    if (eat('+')) x += parseFactor()
                    else if (eat('-')) x -= parseFactor()
                    else if (eat('*')) x *= parseFactor()
                    else if (eat('/')) x /= parseFactor()
                    else break
                }
                eat(')')
            } else if ((ch in '0'..'9') || ch == '.') {
                while ((ch in '0'..'9') || ch == '.') nextChar()
                x = str.substring(startPos, pos).toDouble()
            } else if (ch in 'a'..'z') {
                while (ch in 'a'..'z') nextChar()
                val func = str.substring(startPos, pos)
                val arg = parseFactor()
                x = when (func) {
                    "sqrt" -> sqrt(arg)
                    "sin" -> {
                        val rad = if (mode == AngleMode.DEGREE) Math.toRadians(arg) else arg
                        sin(rad)
                    }
                    "cos" -> {
                        val rad = if (mode == AngleMode.DEGREE) Math.toRadians(arg) else arg
                        cos(rad)
                    }
                    "tan" -> {
                        val rad = if (mode == AngleMode.DEGREE) Math.toRadians(arg) else arg
                        tan(rad)
                    }
                    "log" -> log10(arg)
                    "ln" -> ln(arg)
                    else -> throw RuntimeException("دالة غير معروفة: $func")
                }
            } else {
                throw RuntimeException("رمز غير متوقع: $ch")
            }

            if (eat('^')) {
                val power = parseFactor()
                x = x.pow(power)
            }
            return x
        }

        nextChar()
        var x = parseFactor()
        while (true) {
            if (eat('+')) x += parseFactor()
            else if (eat('-')) x -= parseFactor()
            else if (eat('*')) x *= parseFactor()
            else if (eat('/')) x /= parseFactor()
            else break
        }
        return x
    }

    fun det2x2(a: Double, b: Double, c: Double, d: Double): Double = a * d - b * c

    fun solveQuadratic(a: Double, b: Double, c: Double): Pair<List<String>, String> {
        if (abs(a) < 1e-9) {
            return if (abs(b) < 1e-9) {
                emptyList<String>() to "معادلة غير صالحة"
            } else {
                val root = -c / b
                listOf("س = " + String.format("%.2f", root)) to "معادلة من الدرجة الأولى"
            }
        }
        val delta = b * b - 4 * a * c
        return when {
            delta > 0 -> {
                val r1 = (-b + sqrt(delta)) / (2 * a)
                val r2 = (-b - sqrt(delta)) / (2 * a)
                listOf("س₁ = " + String.format("%.2f", r1), "س₂ = " + String.format("%.2f", r2)) to
                        "المميز Δ = " + String.format("%.2f", delta) + " > 0 (جذران حقيقيان مختلفان)"
            }
            abs(delta) < 1e-9 -> {
                val r = -b / (2 * a)
                listOf("س = " + String.format("%.2f", r)) to "المميز Δ = 0 (جذر حقيقي مكرر)"
            }
            else -> {
                val real = -b / (2 * a)
                val imag = sqrt(-delta) / (2 * a)
                listOf(
                    "س₁ = " + String.format("%.2f", real) + " + " + String.format("%.2f", imag) + "ت",
                    "س₂ = " + String.format("%.2f", real) + " - " + String.format("%.2f", imag) + "ت"
                ) to "المميز Δ = " + String.format("%.2f", delta) + " < 0 (جذران مركبان مترافقان)"
            }
        }
    }
}
