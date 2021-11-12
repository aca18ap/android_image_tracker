package com.lab.lab5_kt

import android.app.Application
import com.lab.lab5_kt.data.ImageRoomDatabase

class ImageApplication: Application() {
    val databaseObj: ImageRoomDatabase by lazy { ImageRoomDatabase.getDatabase(this) }
}
