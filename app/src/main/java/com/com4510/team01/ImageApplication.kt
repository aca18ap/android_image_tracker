package com.com4510.team01

import android.app.Application
import com.com4510.team01.model.data.database.ImageRoomDatabase

class ImageApplication: Application() {
    val databaseObj: ImageRoomDatabase by lazy { ImageRoomDatabase.getDatabase(this) }
}
