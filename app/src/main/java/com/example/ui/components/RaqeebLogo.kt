package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun RaqeebRLogo(
    size: Dp = 80.dp,
    showText: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = RoyalBlueButton.copy(alpha = 0.35f))
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .border(2.dp, Brush.linearGradient(listOf(CyanInteractive, RoyalBlueButton)), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            // High-resolution artistic generated R logo
            Image(
                painter = painterResource(id = R.drawable.raqeeb_r_logo_1792186626631),
                contentDescription = "شعار رقيب للتعليم الثانوي بحرف R",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
        }

        if (showText) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "رقيب للتعليم الثانوي",
                    fontSize = (size.value * 0.24f).coerceIn(16f, 24f).sp,
                    fontWeight = FontWeight.Black,
                    color = NavyTitle,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "«افهم • حل • اختبر • تفوق»",
                    fontSize = (size.value * 0.14f).coerceIn(11f, 14f).sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanInteractive
                )
            }
        }
    }
}
