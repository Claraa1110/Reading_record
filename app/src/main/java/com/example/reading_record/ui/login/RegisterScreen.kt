package com.example.reading_record.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen

@Composable
fun RegisterScreen(navController: NavController) {

    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // 書本 icon
        Image(
            painter = painterResource(id = R.drawable.book_icon),
            contentDescription = null,
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 標題
        Text(
            text = "建立您的帳號",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 副標題
        Text(
            text = "開始追蹤您的閱讀旅程",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFBEBEBE)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // 帳號
        Text(
            text = "帳號",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = account,
            onValueChange = { account = it },
            placeholder = { Text("請輸入帳號", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3A3535),
                unfocusedContainerColor = Color(0xFF3A3535),
                focusedBorderColor = Color(0xFF7B7B7B),
                unfocusedBorderColor = Color(0xFF7B7B7B),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        // 密碼
        Text(
            text = "密碼",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("請輸入密碼", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3A3535),
                unfocusedContainerColor = Color(0xFF3A3535),
                focusedBorderColor = Color(0xFF7B7B7B),
                unfocusedBorderColor = Color(0xFF7B7B7B),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        // 確認密碼
        Text(
            text = "確認密碼",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("請再次輸入密碼", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3A3535),
                unfocusedContainerColor = Color(0xFF3A3535),
                focusedBorderColor = Color(0xFF7B7B7B),
                unfocusedBorderColor = Color(0xFF7B7B7B),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(36.dp))

        // 建立按鈕
        Button(
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6BB7FF),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "建立",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 返回登入
        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "已有帳號？返回登入",
                color = Color(0xFF6BB7FF)
            )
        }
    }
}