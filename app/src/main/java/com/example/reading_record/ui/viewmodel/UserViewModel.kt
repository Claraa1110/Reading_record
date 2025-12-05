package com.example.reading_record.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reading_record.ui.data.AppDatabase
import com.example.reading_record.ui.data.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()

    // 目前登入的使用者
    var currentUser by mutableStateOf<User?>(null)
        private set

    // UI 提示訊息
    var uiMessage by mutableStateOf<String?>(null)
        private set

    // 註冊
    fun register(account: String, pass: String, confirmPass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (account.isBlank() || pass.isBlank()) {
                uiMessage = "欄位不能為空"
                return@launch
            }
            if (pass != confirmPass) {
                uiMessage = "兩次密碼不一致"
                return@launch
            }

            val existUser = userDao.getUserByAccount(account)
            if (existUser != null) {
                uiMessage = "此帳號已被註冊"
            } else {
                val newUser = User(account = account, password = pass, userName = account, imageUri = null)
                userDao.insertUser(newUser)
                uiMessage = "註冊成功，請登入"
                onSuccess()
            }
        }
    }

    // 登入
    fun login(account: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (account.isBlank() || pass.isBlank()) {
                uiMessage = "請輸入帳號密碼"
                return@launch
            }
            val user = userDao.login(account, pass)
            if (user != null) {
                currentUser = user
                uiMessage = null
                onSuccess()
            } else {
                uiMessage = "帳號或密碼錯誤"
            }
        }
    }

    // 更新個人資料
    fun updateProfile(newName: String, newImageUri: Uri?) {
        val user = currentUser ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(
                userName = newName,
                imageUri = newImageUri?.toString()
            )
            userDao.updateUser(updatedUser)
            currentUser = updatedUser
        }
    }

    // 清除訊息
    fun clearMessage() {
        uiMessage = null
    }

    // 登出
    fun logout() {
        currentUser = null
        uiMessage = null
    }
}