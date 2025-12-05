package com.example.reading_record.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument

import com.example.reading_record.ui.login.LoginScreen
import com.example.reading_record.ui.login.RegisterScreen
import com.example.reading_record.ui.home.HomeScreen
import com.example.reading_record.ui.home.AddBookScreen
import com.example.reading_record.ui.profile.ProfileScreen
import com.example.reading_record.ui.profile.EditProfileScreen
import com.example.reading_record.ui.home.BottomNavBar

import com.example.reading_record.viewmodel.UserViewModel
import com.example.reading_record.viewmodel.BookViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddBook : Screen("add_book")
    // ⭐ 新增：編輯書籍路線，後面接著 /{bookId} 代表要傳參數
    object EditBook : Screen("edit_book/{bookId}") {
        fun createRoute(bookId: Int) = "edit_book/$bookId"
    }
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
}

@Composable
fun AppNav(navController: NavHostController) {

    val userViewModel: UserViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 只有在首頁和個人頁面顯示底部導航
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

            composable(Screen.Login.route) {
                LoginScreen(navController, userViewModel)
            }

            composable(Screen.Register.route) {
                RegisterScreen(navController, userViewModel)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController, userViewModel, bookViewModel)
            }

            // 新增書籍 (不帶參數)
            composable(Screen.AddBook.route) {
                AddBookScreen(navController, bookViewModel, userViewModel, bookId = null)
            }

            // ⭐ 編輯書籍 (帶 bookId 參數)
            composable(
                route = Screen.EditBook.route,
                arguments = listOf(navArgument("bookId") { type = NavType.IntType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId")
                AddBookScreen(navController, bookViewModel, userViewModel, bookId = bookId)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController, userViewModel, bookViewModel)
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(navController, userViewModel)
            }
        }
    }
}