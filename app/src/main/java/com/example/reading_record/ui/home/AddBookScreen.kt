package com.example.reading_record.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reading_record.R
import com.example.reading_record.util.copyUriToInternalStorage
import com.example.reading_record.viewmodel.BookViewModel
import com.example.reading_record.viewmodel.UserViewModel

@Composable
fun AddBookScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    bookId: Int? = null // ⭐ 接收 bookId，如果是 null 代表是新增，有值代表是編輯
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // UI 狀態
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("想讀") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ⭐ 如果是編輯模式，載入舊資料
    LaunchedEffect(bookId) {
        if (bookId != null) {
            val book = bookViewModel.getBookById(bookId)
            if (book != null) {
                title = book.title
                author = book.author
                status = book.status
                imageUri = book.imageUri?.let { Uri.parse(it) }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = copyUriToInternalStorage(context, uri)
        }
    }

    val isEditMode = bookId != null
    val pageTitle = if (isEditMode) "編輯書籍" else "新增書籍"
    val saveButtonText = if (isEditMode) "更新書籍" else "儲存書籍"

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
            .verticalScroll(scrollState)
            .padding(28.dp)
    ) {

        Text(
            text = pageTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        // 圖片
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(260.dp)
                .border(BorderStroke(2.dp, Color(0xFF9B9B9B)), shape = RoundedCornerShape(16.dp))
                .clickable { galleryLauncher.launch("image/*") },
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

        Text("書名", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = title, onValueChange = { title = it },
            placeholder = { Text("請輸入書名") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(Modifier.height(20.dp))

        Text("作者", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = author, onValueChange = { author = it },
            placeholder = { Text("請輸入作者") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(Modifier.height(28.dp))

        Text("狀態", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("想讀", "正在閱讀", "已讀").forEach { s ->
                val selected = (s == status)
                Button(
                    onClick = { status = s },
                    modifier = Modifier.weight(1f).height(48.dp),
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

        // ⭐ 儲存/更新按鈕
        Button(
            onClick = {
                val currentUser = userViewModel.currentUser
                if (currentUser == null) {
                    Toast.makeText(context, "請先登入", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (title.isBlank() || author.isBlank()) {
                    Toast.makeText(context, "請至少填寫書名與作者", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (isEditMode && bookId != null) {
                    // 更新
                    bookViewModel.updateBook(bookId, title, author, status, imageUri, currentUser.id)
                    Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show()
                } else {
                    // 新增
                    bookViewModel.addBook(title, author, status, imageUri, currentUser.id)
                    Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show()
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().height(58.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6BB7FF)),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(saveButtonText, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // ⭐ 刪除/取消按鈕
        OutlinedButton(
            onClick = {
                if (isEditMode && bookId != null) {
                    // 刪除邏輯
                    bookViewModel.deleteBook(bookId)
                    Toast.makeText(context, "書籍已刪除", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    // 取消邏輯
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth().height(58.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5555)),
            border = BorderStroke(1.dp, Color(0xFFFF5555)),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(if (isEditMode) "刪除書籍" else "取消", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(32.dp))
    }
}