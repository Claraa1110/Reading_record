package com.example.reading_record.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen

// 模擬書籍資料結構
data class Book(val id: Int, val title: String)

// 模擬書籍列表
val mockBooks = listOf(
    Book(1, "書本 A"),
    Book(2, "書本 B"),
    Book(3, "書本 C"),
    Book(4, "書本 D"),
    Book(5, "書本 E")
)

@Composable
fun HomeScreen(navController: NavController) {

    val darkBackground = Color(0xFF1E1B1B)
    val textWhite = Color.White
    val categoryHeaderColor = Color.White

    // ⭐ 修正：Home頁面現在只負責內容，Scaffold由AppNav提供
    Scaffold(
        containerColor = darkBackground,
        // ❌ 移除 bottomBar = { BottomNavBar(navController = navController) }
        // 避免重複顯示導航列
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                // ⭐ 傳遞由 AppNav Scaffold 提供的內邊距
                .padding(paddingValues)
        ) {

            // ⭐ 頂部欄：用戶頭像和標題
            TopBar(textWhite = textWhite)

            // ⭐ 滾動內容區域
            Column(modifier = Modifier.fillMaxSize()) {
                BookCategories(
                    title = "正在閱讀",
                    books = mockBooks,
                    categoryHeaderColor = categoryHeaderColor
                )

                BookCategories(
                    title = "已讀",
                    books = mockBooks,
                    categoryHeaderColor = categoryHeaderColor
                )

                BookCategories(
                    title = "未來清單",
                    books = mockBooks,
                    categoryHeaderColor = categoryHeaderColor
                )
            }
        }
    }
}

// === 輔助 Composable 函數 ===

@Composable
fun TopBar(textWhite: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_placeholder),
            contentDescription = "User Profile",
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = "我的書櫃",
            style = MaterialTheme.typography.headlineLarge,
            color = textWhite,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BookCategories(title: String, books: List<Book>, categoryHeaderColor: Color) {

    Text(
        text = title,
        color = categoryHeaderColor,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            BookCoverItem(book = book)
        }
    }
}

@Composable
fun BookCoverItem(book: Book) {
    Image(
        painter = painterResource(id = R.drawable.book_placeholder),
        contentDescription = book.title,
        modifier = Modifier
            .width(90.dp)
            .height(135.dp)
    )
}

// ⭐ 底部導航欄定義：此函式必須保持在此處或移至獨立檔案，以便 AppNav 呼叫
@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFF0F0F0),
        contentColor = Color.Black,
        modifier = Modifier.height(70.dp)
    ) {
        // 1. 書櫃
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_book_filled),
                    contentDescription = "書櫃",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    "書櫃",
                    fontSize = 14.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E1B1B),
                selectedTextColor = Color(0xFF1E1B1B),
                unselectedIconColor = Color(0xFF757575),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )

        // 2. 新增 (中間藍色圓形按鈕)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddBook.route) },
                containerColor = Color(0xFF6BB7FF),
                modifier = Modifier.size(65.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "新增",
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        // 3. 個人
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Profile.route) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_person_outline),
                    contentDescription = "個人",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    "個人",
                    fontSize = 14.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E1B1B),
                selectedTextColor = Color(0xFF1E1B1B),
                unselectedIconColor = Color(0xFF757575),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )
    }
}