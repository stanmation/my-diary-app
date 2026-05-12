package com.stanmation.mydiary.utilities

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Locale


fun Uri.extractPhotoDate(context: Context): Long? {
    return try {
        context.contentResolver.openInputStream(this)?.use { input ->
            val exif = ExifInterface(input)
            val dateString = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                ?: exif.getAttribute(ExifInterface.TAG_DATETIME)

            dateString?.let {
                val formatter = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
                formatter.parse(it)?.time
            }
        }
    } catch (_: Exception) {
        null
    }
}