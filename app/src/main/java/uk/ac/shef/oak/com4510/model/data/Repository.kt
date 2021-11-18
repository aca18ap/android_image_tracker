package uk.ac.shef.oak.com4510.model.data

import android.app.Application
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.model.data.database.ImageDataDao
import uk.ac.shef.oak.com4510.model.data.database.ImageRoomDatabase
import kotlinx.coroutines.*

class Repository(application: Application) {
    private var imageDataDao: ImageDataDao? = null

    init {
        val db: ImageRoomDatabase? = ImageRoomDatabase.getDatabase(application)
        if (db != null) { imageDataDao = db.imageDataDao() }
    }

    //Separate constructor that allows passing a custom dao. For testing
    constructor(dataSource : ImageDataDao,app : Application) : this(app)
    {
        imageDataDao = dataSource
    }

    /**
     * Returns every image in the databases
     */
    suspend fun getAllImages() : List<ImageData>? = withContext(Dispatchers.IO)
    {
        imageDataDao?.getItems()
    }

    suspend fun search(query : String) : List<ImageData>? = withContext(Dispatchers.IO)
    {
        imageDataDao?.search(query)
    }

    /**
     * Inserts an imageData object into the database. Returns the id it was assigned to
     */
    suspend fun insertDataReturnId(imageData: ImageData): Int = coroutineScope {
        var defferedID = async { imageDataDao?.insert(imageData) }
        defferedID.await()?.toInt()!!
    }

    /**
     *  Updates the database given an imageData object
     */
    suspend fun updateImage(imageData : ImageData) = withContext(Dispatchers.IO)
    {
        imageDataDao?.update(imageData)
    }



}