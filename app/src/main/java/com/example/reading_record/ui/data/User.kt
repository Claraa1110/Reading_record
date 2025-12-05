package com.example.reading_record.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val account: String,
    val password: String,
    val userName: String,
    val imageUri: String?
)