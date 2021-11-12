package com.com4510.team01.model.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.com4510.team01.model.data.database.ImageData
import com.com4510.team01.model.data.database.ImageDataDao
import com.com4510.team01.model.data.database.ImageRoomDatabase
import kotlinx.coroutines.*

class Repository(application: Application) {
    private var imageDataDao: ImageDataDao? = null

    init {
        val db: ImageRoomDatabase? = ImageRoomDatabase.getDatabase(application)
        if (db != null) { imageDataDao = db.imageDataDao() }
    }

    /**
     * Returns every image in the databases
     */
    suspend fun getAllImages() : List<ImageData>? = withContext(Dispatchers.IO)
    {
        imageDataDao?.getItems()
    }

    /**
     * Inserts an imageData object into the database. Returns the id it was assigned to
     */
    suspend fun insertDataReturnId(imageData: ImageData): Int = coroutineScope {
        var defferedID = async { imageDataDao?.insert(imageData) }
        defferedID.await()?.toInt()!!
    }



}