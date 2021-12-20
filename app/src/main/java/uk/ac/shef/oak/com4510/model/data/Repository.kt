package uk.ac.shef.oak.com4510.model.data

import android.app.Application
import android.media.Image
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.model.data.database.*
import uk.ac.shef.oak.com4510.viewModel.OrderBy

/**
 * Class used by the viewmodel to abstract accessing data from the database. Not to be used by any class outside the viewmodel
 */
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
     * Returns an image given an id
     * @param
     */
    suspend fun getImage(imageId : Int) = withContext(Dispatchers.IO)
    {
        imageDataDao?.getItem(imageId)
    }

    /**
     * Returns every image in the databases
     * @param order Order to return the images in
     * @return Every image in the database
     */
    suspend fun getAllImages(order : OrderBy) : List<ImageData>? = withContext(Dispatchers.IO)
    {
        val allImagesSorted = when(order)
        {
            OrderBy.NOPARTICULARORDER -> imageDataDao?.getItems()
            OrderBy.ASC -> imageDataDao?.getItemsTimeASC()
            else -> imageDataDao?.getItemsTimeDESC()
        }
        allImagesSorted
    }
    /**
     * Returns images whose title or description contain words from the query
     * @param query Query string to use when searching the database
     * @return List of images whose title or description contain words from the query
     */
    suspend fun search(query : String, order : OrderBy) : List<ImageData>? = withContext(Dispatchers.IO)
    {
        val matchedImages = when(order)
        {
            OrderBy.NOPARTICULARORDER -> imageDataDao?.search(query)
            OrderBy.ASC -> imageDataDao?.searchOrderByTimeStampASC(query)
            else -> imageDataDao?.searchOrderByTimeStampDESC(query)
        }
        matchedImages
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
     * @param entryDataId Id of the entry being retrieved
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

    /**
     * Given a tripData ID and entry measurements, creates and inserts an Entry into the database.
     * WARNING: This does block the UI thread, to be used sparingly.
     *
     * @param tripDataID Id of the trip this entry is to be linked with
     * @param temperature Temperature taken at the entry position and time
     * @param pressure Pressure measurement taken at the entry position and time
     * @param lat Latitude coordinate of the entry
     * @param lon Longitude coordinate of the entry
     * @return The entry that was inserted into the database
     * @see create_insert_entry for a function that creates and inserts and entry but does not block the UI thread
     */
    fun create_insert_entry_returnEntry(tripDataID: Int, temperature:Float?, pressure:Float?, lat:Double, lon:Double, timestamp:Long) : EntryData = runBlocking{
        val createdEntry = EntryData(
            lat = lat, lon = lon,
            entry_timestamp = timestamp, entry_temperature = temperature,
            entry_pressure = pressure, trip_id = tripDataID)

        val id = insertEntryReturnId(createdEntry)
        createdEntry.id = id
        createdEntry
    }
    /**
     * Given a trip's id and sensor measurements, create and insert an Entry into the database
     *
     * @param tripDataID Id of the trip this entry is to be linked with
     * @param temperature Temperature taken at the entry position and time
     * @param pressure Pressure measurement taken at the entry position and time
     * @param lat Latitude coordinate of the entry
     * @param lon Longitude coordinate of the entry
     * @param timestamp time at which the entry was captured
     */
    suspend fun create_insert_entry(tripDataID: Int, temperature:Float?, pressure:Float?, lat:Double, lon:Double, timestamp:Long)= withContext(Dispatchers.IO){
            val createdEntry = EntryData(
                lat = lat, lon = lon,
                entry_timestamp = timestamp, entry_temperature = temperature,
                entry_pressure = pressure, trip_id = tripDataID)
            insertEntryReturnId(createdEntry)
    }

    // ---------TripData related --------------------------------------
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
    suspend fun getTrip(tripId: Int) = withContext(Dispatchers.IO) {
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