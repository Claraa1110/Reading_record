package com.example.reading_record.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen


@Composable
fun ProfileScreen(navController: NavController) {

    val darkBackground = Color(0xFF1E1B1B)
    val logoutButtonColor = Color(0xFF8B3333)
    val statBorderColor = Color(0xFF6B6B6B)

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

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFB39DDB))
            ,
            contentAlignment = Alignment.Center
        ) {
            // 假設 R.drawable.user_placeholder 存在
            Image(
                painter = painterResource(id = R.drawable.user_placeholder),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "使用者名稱",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(32.dp))

        // ⭐ 3. 統計數據卡片 - 修正區塊
        Row(
            // 移除 IntrinsicSize.Min，讓 weight(1f) 來控制寬度
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // 居中擺放
        ) {
            // ✅ 在 RowScope 內使用 weight(1f) 確保平均分配
            StatsCard(
                "128",
                "已讀",
                statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )
            // 增加固定的間隔
            Spacer(Modifier.width(8.dp))

            StatsCard(
                "5",
                "閱讀中",
                statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )

            Spacer(Modifier.width(8.dp)) // 增加固定的間隔

            StatsCard(
                "32",
                "想讀",
                statBorderColor,
                modifier = Modifier.weight(1f).height(80.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // ⭐ 4. 編輯個人檔案按鈕
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(Icons.Filled.Create, contentDescription = "Edit", tint = Color.White)
                Spacer(Modifier.width(16.dp))
                Text("編輯個人檔案", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ⭐ 5. 登出按鈕
        Button(
            onClick = {
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

// 輔助 Composable 函數：統計數據卡片
@Composable
fun StatsCard(
    count: String,
    label: String,
    borderColor: Color,
    // 接受從 ProfileScreen 傳入的 Modifier
    modifier: Modifier = Modifier
) {
    Column(
        // ✅ 使用傳入的 Modifier 參數，不再包含 weight(1f)
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            count,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            color = Color(0xFFC0C0C0),
            fontSize = 13.sp
        )
    }
}