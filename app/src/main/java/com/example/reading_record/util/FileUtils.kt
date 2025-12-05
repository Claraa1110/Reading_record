package com.example.reading_record.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// 將 Bitmap 存到 App 內部空間
fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
    val stream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.close()
    return Uri.fromFile(file)
}

// 將外部 Uri (如相簿) 的檔案複製到 App 內部空間
fun copyUriToInternalStorage(context: Context, sourceUri: Uri): Uri {
    val inputStream: InputStream? = context.contentResolver.openInputStream(sourceUri)
    val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return Uri.fromFile(file)
}