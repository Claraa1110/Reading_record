package com.example.reading_record.ui.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // ⭐ 新增：用於觀察 Flow
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen
import com.example.reading_record.viewmodel.BookViewModel // ⭐ 新增
import com.example.reading_record.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    bookViewModel: BookViewModel // ⭐ 1. 接收 BookViewModel
) {
    val darkBackground = Color(0xFF2B2929)
    val logoutButtonColor = Color(0xFF8B3333)
    val statBorderColor = Color(0xFF6B6B6B)

    // 從 UserViewModel 讀取使用者資料
    val currentUser = userViewModel.currentUser
    val userName = currentUser?.userName ?: "訪客"
    val userImageUri = currentUser?.imageUri?.let { Uri.parse(it) }

    // ⭐ 2. 觀察書籍列表並計算數量
    val allBooks by bookViewModel.userBooks.collectAsState()

    val readCount = allBooks.count { it.status == "已讀" }
    val readingCount = allBooks.count { it.status == "正在閱讀" }
    val wantToReadCount = allBooks.count { it.status == "想讀" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "個人檔案",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(32.dp))

        // 頭像區塊
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFB39DDB)),
            contentAlignment = Alignment.Center
        ) {
            if (userImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(userImageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = "Default Profile",
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(32.dp))

        // ⭐ 3. 統計數據卡片 (使用計算出的變數)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            StatsCard(
                count = readCount.toString(),
                label = "已讀",
                borderColor = statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )
            Spacer(Modifier.width(8.dp))

            StatsCard(
                count = readingCount.toString(),
                label = "閱讀中",
                borderColor = statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )
            Spacer(Modifier.width(8.dp))

            StatsCard(
                count = wantToReadCount.toString(),
                label = "想讀",
                borderColor = statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // 編輯按鈕
        Button(
            onClick = { navController.navigate(Screen.EditProfile.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF808080)),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Create, contentDescription = "Edit", tint = Color.White)
                Spacer(Modifier.width(16.dp))
                Text("編輯個人檔案", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        // 登出按鈕
        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = logoutButtonColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("登出", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun StatsCard(count: String, label: String, borderColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color(0xFFC0C0C0), fontSize = 13.sp)
    }
}