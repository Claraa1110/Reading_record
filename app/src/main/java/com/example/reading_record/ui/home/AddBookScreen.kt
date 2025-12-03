package com.example.reading_record.ui.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reading_record.R

@Composable
fun AddBookScreen(navController: NavController) {

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("想讀") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // ★ TextField Colors
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF3A3535),
        unfocusedContainerColor = Color(0xFF3A3535),
        focusedBorderColor = Color(0xFF777777),
        unfocusedBorderColor = Color(0xFF777777),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedPlaceholderColor = Color(0xFFCCCCCC),
        unfocusedPlaceholderColor = Color(0xFFCCCCCC)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1B1B))
            .padding(28.dp)
    ) {

        // ⭐ 標題（置中）
        Text(
            text = "新增書籍",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        // ⭐ 圖片區域置中
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(260.dp)
                .border(
                    BorderStroke(2.dp, Color(0xFF9B9B9B)),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.upload_placeholder),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ⭐ 書名
        Text("書名", color = Color.White, fontWeight = FontWeight.Bold)
        // ✅ 間距增加
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("請輸入書名") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(Modifier.height(20.dp))

        // ⭐ 作者
        Text("作者", color = Color.White, fontWeight = FontWeight.Bold)
        // ✅ 間距增加
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            placeholder = { Text("請輸入作者") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(Modifier.height(28.dp))

        // ⭐ 狀態
        Text("狀態", color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        // 狀態按鈕列
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val statusList = listOf("想讀", "正在閱讀", "已讀")

            statusList.forEach { s ->
                val selected = (s == status)

                Button(
                    onClick = { status = s },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) Color(0xFF6BB7FF) else Color(0xFFBEBEBE),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(s)
                }

                Spacer(Modifier.width(8.dp))
            }
        }

        Spacer(Modifier.height(32.dp))

        // ⭐ 儲存按鈕
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6BB7FF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("儲存書籍", fontWeight = FontWeight.Bold)
        }
    }
}