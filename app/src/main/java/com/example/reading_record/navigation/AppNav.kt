package com.example.reading_record.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
// ⭐ 修正：導入 padding 函式
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

import com.example.reading_record.ui.login.LoginScreen
import com.example.reading_record.ui.login.RegisterScreen
import com.example.reading_record.ui.home.HomeScreen
import com.example.reading_record.ui.home.AddBookScreen
import com.example.reading_record.ui.profile.ProfileScreen
import com.example.reading_record.ui.profile.EditProfileScreen
import com.example.reading_record.ui.home.BottomNavBar // 引入 BottomNavBar

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddBook : Screen("add_book") // 不顯示底部導航列
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
}

@Composable
fun AppNav(navController: NavHostController) {

    // 取得當前導航路由狀態
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ⭐ 決定是否顯示底部導航列
    val shouldShowBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                // 將 BottomNavBar 放在這裡，這樣它就會固定顯示
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            // ⭐ 使用導入的 Modifier.padding 函式來應用內邊距
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Login.route) {
                LoginScreen(navController)
            }

            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.AddBook.route) {
                AddBookScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(navController)
            }
        }
    }
}