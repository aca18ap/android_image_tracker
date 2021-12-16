package uk.ac.shef.oak.com4510.model.data

import android.app.Application
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.model.data.database.*

class Repository(application: Application) {
    private var imageDataDao: ImageDataDao? = null
    private var entryDataDao : EntryDataDao? = null
    private var tripDataDao : TripDataDao? = null

    init {
        val db: ImageRoomDatabase? = ImageRoomDatabase.getDatabase(application)
        if (db != null) { imageDataDao = db.imageDataDao() }
    }

    //Separate constructor that allows passing a custom dao. For testing
    constructor(dataSource : ImageDataDao, app : Application) : this(app)
    {
        imageDataDao = dataSource
    }

    // ---------ImageData related -------------------------------------
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
    suspend fun deleteImage(imageData: ImageData) = withContext(Dispatchers.IO)
    {
        imageDataDao?.delete(imageData)
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
    // ---------EntryData related -------------------------------------
    /**
     * Get all entries for a given trip
     */
    suspend fun getEntriesOfTrip(tripData : TripData) : List<EntryData>? = withContext(Dispatchers.IO)
    {
        entryDataDao?.getEntriesForTrip(tripData.id)
    }



    // ---------TripData related --------------------------------------
    suspend fun getAllTrips() : List<TripData>? = withContext(Dispatchers.IO)
    {
        tripDataDao?.getItems()
    }

}