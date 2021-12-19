package uk.ac.shef.oak.com4510.model.data

import android.app.Application
import android.media.Image
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.model.data.database.*

class Repository(application: Application) {
    private var imageDataDao: ImageDataDao? = null
    private var entryDataDao : EntryDataDao? = null
    private var tripDataDao : TripDataDao? = null

    init {
        val db: ImageRoomDatabase? = ImageRoomDatabase.getDatabase(application)
        if (db != null) {
            imageDataDao = db.imageDataDao()
            entryDataDao = db.entryDataDao()
            tripDataDao = db.tripDataDao()
        }
    }

    //Separate constructor that allows passing a custom dao. For testing
    constructor(dataSource : ImageDataDao, app : Application) : this(app)
    {
        imageDataDao = dataSource
    }

    // ---------ImageData related -------------------------------------
    /**
     * Returns every image in the databases
     * @return Every image in the database
     */
    suspend fun getAllImages() : List<ImageData>? = withContext(Dispatchers.IO)
    {
        imageDataDao?.getItems()
    }
    /**
     * Returns images whose title or description contain words from the query
     * @param query Query string to use when searching the database
     * @return List of images whose title or description contain words from the query
     */
    suspend fun search(query : String) : List<ImageData>? = withContext(Dispatchers.IO)
    {
        imageDataDao?.search(query)
    }

    /**
     * Deletes an image from the database
     * @param imageData Image to be deleted from the database
     */
    suspend fun deleteImage(imageData: ImageData) = withContext(Dispatchers.IO)
    {
        imageDataDao?.delete(imageData)
    }

    /**
     * Gets all images of a given entry
     * @param entryDataID id of entry whose associated images are to be retrieved
     * @return All images corresponding to the entry whose id = [entryDataID]
     */
    suspend fun getImagesOfEntry(entryDataID: Int) = withContext(Dispatchers.IO)
    {
        imageDataDao?.getEntryImages(entryDataID)
    }

    /**
     * Inserts a list of images in the database
     * @param listImageData List of images to be inserted
     */
    suspend fun insertListImageData(listImageData : List<ImageData>) = withContext(Dispatchers.IO)
    {
        imageDataDao?.insertList(listImageData)
    }

    /**
     * Inserts an imageData object into the database. Returns the id it was assigned to
     * @param imageData ImageData objected to be inserted
     * @return id of the image that got inserted
     */
    suspend fun insertImageReturnId(imageData: ImageData): Int = coroutineScope {
        imageDataDao?.insert(imageData)!!.toInt()
    }

    /**
     *  Updates the database given an imageData object
     *  @param imageData ImageData object to replace the previous ImageData object with the same id.
     */
    suspend fun updateImage(imageData : ImageData) = withContext(Dispatchers.IO)
    {
        imageDataDao?.update(imageData)
    }
    // ---------EntryData related -------------------------------------

    /**
     * Returns an entry with the given id
     * @param entryDataId Id of the entry being retreived
     * @return Entry with the given id if it exists, null otherwise
     */
    suspend fun getEntry(entryDataId : Int) = withContext(Dispatchers.IO){
        entryDataDao?.getItem(entryDataId)
    }

    /**
     * Delete an entry from the database
     * @param entryData EntryData to delete
     */
    suspend fun deleteEntry(entryData : EntryData) = withContext(Dispatchers.IO)
    {
        entryDataDao?.delete(entryData)
    }

    /**
     * Get all entries for a given trip
     * @param tripDataID Trip id of the trip whose entries are to be returned
     * @return All entries corresponding to the trip whose id = tripDataID
     */
    suspend fun getEntriesOfTrip(tripDataID : Int) : List<EntryData>? = withContext(Dispatchers.IO)
    {
        entryDataDao?.getEntriesForTrip(tripDataID)
    }

    /**
     * Inserts an entry into the database and returns an id
     * @param entryData Entry to be inserted
     * @return Id of the EntryData that just got inserted
     */
    suspend fun insertEntryReturnId(entryData: EntryData): Int = withContext(Dispatchers.IO) {
        entryDataDao?.insert(entryData)!!.toInt()
    }

    /**
     * Gets the last entry in the database by id
     * @return Last entry in the database by id
     */
    suspend fun getLastEntryById() = withContext(Dispatchers.IO)
    {
        entryDataDao?.getLastEntryById()
    }




    // ---------TripData related --------------------------------------

    /**
     *
     */

    /**
     * Return all trips in the database
     * @return All trips in the database
     */
    suspend fun getAllTrips() : List<TripData>? = withContext(Dispatchers.IO)
    {
        tripDataDao?.getItems()
    }
    /**
     * Retrieve a trip object by id
     * @param tripId Id of object to be retrieved
     * @return Trip object corresponding to tripId
     */
    suspend fun getTrip(tripId: Int) = coroutineScope {
        tripDataDao?.getItem(tripId)
    }

    /**
     * Inserts a trip into the database
     * @param tripData Trip to be inserted
     * @return Id of the inserted trip
     */
    suspend fun insertTripReturnId(tripData: TripData): Int? = withContext(Dispatchers.IO) {
        tripDataDao?.insert(tripData)!!.toInt()
    }

    /**
     * Deletes a trip from the database
     * @param tripData Trip to be deleted
     */
    suspend fun deleteTrip(tripData: TripData) = withContext(Dispatchers.IO)
    {
        tripDataDao?.delete(tripData)
    }

}