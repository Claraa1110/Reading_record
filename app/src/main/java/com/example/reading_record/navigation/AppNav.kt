package com.example.reading_record.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel // ⭐ 1. 記得加入這個 import
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import com.example.reading_record.ui.login.LoginScreen
import com.example.reading_record.ui.login.RegisterScreen
import com.example.reading_record.ui.home.HomeScreen
import com.example.reading_record.ui.home.AddBookScreen
import com.example.reading_record.ui.profile.ProfileScreen
import com.example.reading_record.ui.profile.EditProfileScreen
import com.example.reading_record.ui.home.BottomNavBar

// ⭐ 2. 假設你的 ViewModel 在這個 package，如果不對請自行修正 import
import com.example.reading_record.viewmodel.UserViewModel
import com.example.reading_record.viewmodel.BookViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddBook : Screen("add_book")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
}

@Composable
fun AppNav(navController: NavHostController) {

    // ⭐ 3. 在這裡初始化 ViewModels
    val userViewModel: UserViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            // ⭐ 4. 將 ViewModel 傳入各個頁面
            // 根據你的錯誤訊息，這些頁面現在都需要 ViewModel

            composable(Screen.Login.route) {
                LoginScreen(navController, userViewModel = userViewModel)
            }

            composable(Screen.Register.route) {
                RegisterScreen(navController, userViewModel = userViewModel)
            }

            composable(Screen.Home.route) {
                // Home 通常同時需要 User 和 Book 資料
                HomeScreen(
                    navController,
                    userViewModel = userViewModel,
                    bookViewModel = bookViewModel
                )
            }

            composable(Screen.AddBook.route) {
                // 根據你上次提供的程式碼，AddBookScreen 目前只有一個參數
                // 如果之後你也改了 AddBookScreen 來儲存書籍，記得這裡也要補上 bookViewModel
                AddBookScreen(navController)
            }

            composable(Screen.Profile.route) {
                // Profile 通常需要 User 資料，若有統計圖表可能也需要 Book 資料
                ProfileScreen(
                    navController,
                    userViewModel = userViewModel,
                    bookViewModel = bookViewModel
                )
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(navController, userViewModel = userViewModel)
            }
        }
    }
}
