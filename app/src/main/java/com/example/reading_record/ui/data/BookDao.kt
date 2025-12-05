package com.example.reading_record.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books WHERE userId = :userId")
    fun getBooksByUser(userId: Int): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): Book?
}