package com.example.reading_record.ui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE account = :account AND password = :password LIMIT 1")
    suspend fun login(account: String, password: String): User?

    @Query("SELECT * FROM users WHERE account = :account LIMIT 1")
    suspend fun getUserByAccount(account: String): User?

    @Update
    suspend fun updateUser(user: User)
}