package com.stanmation.mydiary.utilities

import android.net.Uri
import com.stanmation.mydiary.models.PhotoItem

fun Uri.toPhotoItem(): PhotoItem {
    return PhotoItem(
        id = this.toString(),
        path = this.toString(),
        dateMillis = System.currentTimeMillis()
    )
}