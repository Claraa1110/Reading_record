package com.example.reading_record.ui.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.reading_record.R
import com.example.reading_record.navigation.Screen
import com.example.reading_record.ui.data.Book
import com.example.reading_record.ui.data.User // 引入 User 資料結構
import com.example.reading_record.viewmodel.BookViewModel
import com.example.reading_record.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    bookViewModel: BookViewModel
) {
    val darkBackground = Color(0xFF1E1B1B)
    val textWhite = Color.White
    val categoryHeaderColor = Color.White

    // 取得當前使用者資料 (為了顯示頭像)
    val currentUser = userViewModel.currentUser

    // 載入該使用者的書籍
    LaunchedEffect(currentUser) {
        currentUser?.let { bookViewModel.loadBooks(it.id) }
    }

    val allBooks by bookViewModel.userBooks.collectAsState()

    Scaffold(
        containerColor = darkBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // ⭐ 將 currentUser 傳入 TopBar 以顯示正確頭像
            TopBar(textWhite = textWhite, currentUser = currentUser)

            if (allBooks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("目前沒有書籍，點擊下方按鈕新增", color = Color.Gray)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    BookCategories(
                        title = "正在閱讀",
                        books = allBooks.filter { it.status == "正在閱讀" },
                        categoryHeaderColor = categoryHeaderColor,
                        navController = navController
                    )

                    BookCategories(
                        title = "已讀",
                        books = allBooks.filter { it.status == "已讀" },
                        categoryHeaderColor = categoryHeaderColor,
                        navController = navController
                    )

                    BookCategories(
                        title = "想讀",
                        books = allBooks.filter { it.status == "想讀" },
                        categoryHeaderColor = categoryHeaderColor,
                        navController = navController
                    )
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun TopBar(textWhite: Color, currentUser: User?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ⭐ 頭像邏輯修正：讀取 User 的 imageUri
        val imageUri = currentUser?.imageUri?.let { Uri.parse(it) }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray) // 圖片載入前的底色
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = "Default Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

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
fun BookCategories(
    title: String,
    books: List<Book>,
    categoryHeaderColor: Color,
    navController: NavController
) {
    if (books.isNotEmpty()) {
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
                BookCoverItem(book = book) {
                    navController.navigate(Screen.EditBook.createRoute(book.id))
                }
            }
        }
    }
}

@Composable
fun BookCoverItem(book: Book, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        if (book.imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(book.imageUri)),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .height(135.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.book_placeholder),
                contentDescription = book.title,
                modifier = Modifier
                    .width(90.dp)
                    .height(135.dp)
            )
        }
        // ⭐ 這裡移除了 Text (書名)，現在只會顯示封面圖片
    }
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
        // 1. 書櫃按鈕
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
            icon = { Icon(painterResource(id = R.drawable.ic_book_filled), contentDescription = "書櫃", modifier = Modifier.size(30.dp)) },
            label = { Text("書櫃", fontSize = 14.sp) },
            // 選中時深色，未選中灰色
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E1B1B),
                selectedTextColor = Color(0xFF1E1B1B),
                unselectedIconColor = Color(0xFF757575),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )

        // 2. 中間圓形新增按鈕
        Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddBook.route) },
                containerColor = Color(0xFF6BB7FF),
                modifier = Modifier.size(65.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "新增", tint = Color.White, modifier = Modifier.size(35.dp))
            }
        }

        // 3. 個人按鈕
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
            icon = { Icon(painterResource(id = R.drawable.ic_person_outline), contentDescription = "個人", modifier = Modifier.size(30.dp)) },
            label = { Text("個人", fontSize = 14.sp) },
            // 選中時深色，未選中灰色
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