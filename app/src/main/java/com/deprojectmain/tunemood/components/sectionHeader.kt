package com.deprojectmain.tunemood.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 20.sp, // Increased font size for section headers
        fontWeight = FontWeight.Bold, // Bold font for better emphasis
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}