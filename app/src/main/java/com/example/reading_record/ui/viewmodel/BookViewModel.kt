package com.example.reading_record.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reading_record.ui.data.AppDatabase
import com.example.reading_record.ui.data.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val bookDao = db.bookDao()

    // 儲存所有書籍的列表 (透過 Flow 自動更新)
    private val _userBooks = MutableStateFlow<List<Book>>(emptyList())
    val userBooks: StateFlow<List<Book>> = _userBooks.asStateFlow()

    // 載入特定使用者的書籍
    fun loadBooks(userId: Int) {
        viewModelScope.launch {
            bookDao.getBooksByUser(userId).collectLatest { books ->
                _userBooks.value = books
            }
        }
    }

    // 新增書籍
    fun addBook(title: String, author: String, status: String, imageUri: Uri?, userId: Int) {
        viewModelScope.launch {
            val newBook = Book(
                title = title,
                author = author,
                status = status,
                imageUri = imageUri?.toString(),
                userId = userId
            )
            bookDao.insertBook(newBook)
        }
    }

    // 更新書籍
    fun updateBook(bookId: Int, title: String, author: String, status: String, imageUri: Uri?, userId: Int) {
        viewModelScope.launch {
            val updatedBook = Book(
                id = bookId,
                title = title,
                author = author,
                status = status,
                imageUri = imageUri?.toString(),
                userId = userId
            )
            bookDao.updateBook(updatedBook)
        }
    }

    // 刪除書籍
    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            // 先取得該本書的完整資料才能刪除
            val book = bookDao.getBookById(bookId)
            if (book != null) {
                bookDao.deleteBook(book)
            }
        }
    }

    // 取得單一書籍 (用於編輯時回填資料)
    suspend fun getBookById(bookId: Int): Book? {
        return bookDao.getBookById(bookId)
    }
}