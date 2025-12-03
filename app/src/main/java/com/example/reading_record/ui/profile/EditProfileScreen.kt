package com.example.reading_record.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EditProfileScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }

    // 顏色定義
    val darkBackground = Color(0xFF1E1B1B)
    val textFieldColor = Color(0xFF3A3535)
    val primaryButtonColor = Color(0xFF6BB7FF)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = textFieldColor,
        unfocusedContainerColor = textFieldColor,
        focusedBorderColor = Color(0xFF777777),
        unfocusedBorderColor = Color(0xFF777777),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color(0xFFCCCCCC)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground) // 設置深色背景
            .padding(24.dp)
    ) {

        Text(
            "編輯個人檔案",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(32.dp)) // 增加間距

        Text(
            "暱稱",
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("請輸入暱稱", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(Modifier.height(32.dp)) // 增加間距

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryButtonColor)
        ) {
            Text("儲存變更", fontWeight = FontWeight.Bold)
        }
    }
}