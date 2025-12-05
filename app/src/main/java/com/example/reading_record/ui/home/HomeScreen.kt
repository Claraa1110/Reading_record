package com.example.reading_record.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape // ⭐ 記得加入這個 import
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen
import com.example.reading_record.viewmodel.UserViewModel
import com.example.reading_record.viewmodel.BookViewModel

// 模擬書籍資料結構
data class Book(val id: Int, val title: String)

// ⭐ 修正 1：書櫃一開始要是空的
val mockBooks = listOf<Book>()

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    bookViewModel: BookViewModel
) {

    val darkBackground = Color(0xFF1E1B1B)
    val textWhite = Color.White
    val categoryHeaderColor = Color.White

    Scaffold(
        containerColor = darkBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            TopBar(textWhite = textWhite)

            // 如果書櫃是空的，可以顯示一個提示文字，或是保持空白
            if (mockBooks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("目前沒有書籍", color = Color.Gray)
                }
            } else {
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

@Composable
fun BottomNavBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFF0F0F0),
        contentColor = Color.Black,
        modifier = Modifier.height(70.dp)
    ) {
        // 1. 書櫃
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                if (currentRoute != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_book_filled),
                    contentDescription = "書櫃",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text("書櫃", fontSize = 14.sp) },
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
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddBook.route) },
                containerColor = Color(0xFF6BB7FF),
                modifier = Modifier.size(65.dp),
                shape = CircleShape // ⭐ 修正 2：強制設定為圓形
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
            selected = currentRoute == Screen.Profile.route,
            onClick = {
                if (currentRoute != Screen.Profile.route) {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_person_outline),
                    contentDescription = "個人",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text("個人", fontSize = 14.sp) },
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