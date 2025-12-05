package com.example.reading_record.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen
import com.example.reading_record.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val uiMessage = userViewModel.uiMessage

    LaunchedEffect(uiMessage) {
        if (uiMessage != null) {
            Toast.makeText(context, uiMessage, Toast.LENGTH_SHORT).show()
            userViewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929))
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))
        Image(painter = painterResource(id = R.drawable.book_icon), contentDescription = null, modifier = Modifier.size(140.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Welcome!", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(40.dp))

        Text("帳號", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = account, onValueChange = { account = it },
            placeholder = { Text("請輸入帳號", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3A3535), unfocusedContainerColor = Color(0xFF3A3535),
                focusedBorderColor = Color(0xFF7B7B7B), unfocusedBorderColor = Color(0xFF7B7B7B),
                cursorColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(22.dp))

        Text("密碼", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            placeholder = { Text("請輸入密碼", color = Color(0xFFCCCCCC)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3A3535), unfocusedContainerColor = Color(0xFF3A3535),
                focusedBorderColor = Color(0xFF7B7B7B), unfocusedBorderColor = Color(0xFF7B7B7B),
                cursorColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                Text("還沒有帳號？", color = Color(0xFF57A5F8))
            }
        }
        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                userViewModel.login(account, password) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(58.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6BB7FF), contentColor = Color.White),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("登入", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}