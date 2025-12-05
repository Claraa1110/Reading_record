package com.example.reading_record.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reading_record.R
// ⭐ 修改這裡：匯入工具函式
import com.example.reading_record.util.copyUriToInternalStorage
import com.example.reading_record.util.saveBitmapToInternalStorage
import com.example.reading_record.viewmodel.UserViewModel

@Composable
fun EditProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val currentUser = userViewModel.currentUser

    var name by remember { mutableStateOf(currentUser?.userName ?: "") }
    var selectedImageUri by remember {
        mutableStateOf(currentUser?.imageUri?.let { Uri.parse(it) })
    }
    var showImageOptionDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val savedUri = copyUriToInternalStorage(context, uri)
            selectedImageUri = savedUri
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToInternalStorage(context, bitmap)
            selectedImageUri = uri
        }
    }

    // ... (其餘 UI 程式碼保持不變，直接複製之前的即可) ...
    // ... 為了節省篇幅，中間的 UI 佈局程式碼與上一次回答相同 ...

    val darkBackground = Color(0xFF2B2929)
    val textFieldColor = Color(0xFF3A3535)
    val primaryButtonColor = Color(0xFF6BB7FF)

    Column(
        modifier = Modifier.fillMaxSize().background(darkBackground).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (頂部與頭像區塊) ...
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("編輯個人檔案", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(40.dp))

        Box(contentAlignment = Alignment.Center) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.Gray)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = "Default",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFFE1BEE7))
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset((-4).dp, (-4).dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(primaryButtonColor)
                    .border(2.dp, darkBackground, CircleShape)
                    .clickable { showImageOptionDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // ... (輸入框與按鈕) ...
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("暱稱", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                placeholder = { Text("請輸入暱稱", color = Color(0xFFCCCCCC)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = textFieldColor, unfocusedContainerColor = textFieldColor,
                    focusedBorderColor = Color(0xFF777777), unfocusedBorderColor = Color(0xFF777777),
                    cursorColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp), singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                userViewModel.updateProfile(name, selectedImageUri)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryButtonColor, contentColor = Color.White),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("儲存變更", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }

    if (showImageOptionDialog) {
        AlertDialog(
            onDismissRequest = { showImageOptionDialog = false },
            title = { Text("更換大頭貼") },
            text = {
                Column {
                    ListItem(headlineContent = { Text("從相簿選擇") }, leadingContent = { Icon(Icons.Default.PhotoLibrary, null) }, modifier = Modifier.clickable { showImageOptionDialog = false; galleryLauncher.launch("image/*") })
                    ListItem(headlineContent = { Text("拍照") }, leadingContent = { Icon(Icons.Default.CameraAlt, null) }, modifier = Modifier.clickable { showImageOptionDialog = false; cameraLauncher.launch(null) })
                }
            },
            confirmButton = { TextButton(onClick = { showImageOptionDialog = false }) { Text("取消") } }
        )
    }
}
// ⭐ 注意：這裡不要再寫 saveBitmapToInternalStorage 等函式了，因為已經移到 FileUtils.kt