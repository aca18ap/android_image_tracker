package uk.ac.shef.oak.com4510.viewModel


import android.app.Application
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import pl.aprilapps.easyphotopicker.MediaFile
import uk.ac.shef.oak.com4510.model.data.Repository
import uk.ac.shef.oak.com4510.model.data.database.EntryData
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.model.data.database.TripData
import uk.ac.shef.oak.com4510.util.append
import uk.ac.shef.oak.com4510.util.convertToImageDataWithoutId
import uk.ac.shef.oak.com4510.util.sanitizeSearchQuery

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)

    //Separate constructor to allow passing a different repository. For testing
    constructor(repository: Repository, app : Application) : this(app) {
        mRepository = repository
    }

    /**
     * Observable list of images. Contains all images a user has taken or added to the Travel app represented as ImageData.
     */
    private val _imageList: MutableLiveData<MutableList<ImageData>> = MutableLiveData<MutableList<ImageData>>()
    val imageList : LiveData<MutableList<ImageData>> get() = _imageList

    /**
     * Observable list of images to be used with searching. The search function given a string updates this livedata with
     * a list of all ImageData that contains one of the keywords of the string in either the Title or its Description
     * */
    private val _searchResults  = MutableLiveData<MutableList<ImageData>>()
    val searchResults : LiveData<MutableList<ImageData>>
        get() = _searchResults


    /**
     * Observable list of pairs of (TripData,ImageData?). The ImageData represents one of the images on the given trip if there is any, otherwise null
     */
    private val _tripsSearchResults = MutableLiveData<MutableList<Pair<TripData,ImageData?>>>()
    val tripsSearchResults : LiveData<MutableList<Pair<TripData,ImageData?>>>
        get() = _tripsSearchResults

    /**
     * Observable list of (EntryData,List<ImageData>) corresponding to a particular trip. The List<ImageData> represents the list of images associated with each entry,
     * if. If there are none, it is empty
     */
    private val _entriesOfTrip = MutableLiveData<MutableList<Pair<EntryData,List<ImageData>>>>()
    val entriesOfTrip : LiveData<MutableList<Pair<EntryData,List<ImageData>>>>
        get() = _entriesOfTrip





    // To do: Find a way not to block the ui thread here. Worst case scenario provide a function that inserts multiple ImageData's at a time each on its own coroutine
    /**
     * Inserts an imageData into the database and returns the id it was associated with. Warning: This does block the UI thread.
     * This does not update the imageList livedata object
     */
    fun insertImageReturnId(imageData: ImageData): Int = runBlocking{
        var deferredId = async { mRepository.insertImageReturnId(imageData) }
        deferredId.await()
    }

    /**
     * Handles the photos returned by EasyImage. Inserts an array of MediaFiles into the database and also changes the imageList livedata
     */
    fun debug_insertArrayMediaFiles(mediaFileArray: Array<MediaFile>) {
        var imageDataList = mediaFileArray.convertToImageDataWithoutId()
        insertAndUpdateImageDataList(imageDataList)
        _imageList.append(imageDataList)
    }
    /**
     * Handles the photos returned by EasyImage. Inserts an array of MediaFiles into the database and also changes the imageList livedata
     * Also associates each image with an entry
     */
    fun insertArrayMediaFilesWithEntry(mediaFileArray: Array<MediaFile>, entryData : EntryData) {
        var imageDataList = mediaFileArray.convertToImageDataWithoutId()
        insertAndUpdateImageDataListWithEntry(imageDataList,entryData)
        _imageList.append(imageDataList)
    }

    /**
     * Given an ImageData and an Entry, associates them in the database
     */
    fun associateImageDataWithEntry(imageData: ImageData,entryData: EntryData)
    {
        imageData.entry_id = entryData.id
        viewModelScope.launch(Dispatchers.IO){
            mRepository.updateImage(imageData)
        }
    }

    /**
     * Updates an imageData in the database. TO ADD: Functionality for updating position
     */
    fun updateImageInDatabase(imageData : ImageData, title : String? = null, description : String? = null,entry_id : Int? = null)
    {
        var updatedImage = ImageData(imageData.id,
            imageData.imageUri,
            title ?: imageData.imageTitle,
            description ?: imageData.imageDescription,
            imageData.thumbnailUri,
            imageData.position,
            entry_id ?: imageData.entry_id)
        updatedImage.thumbnail = imageData.thumbnail
        viewModelScope.launch {
            mRepository.updateImage(updatedImage)
        }
    }



    fun initImagesList()
    {
        initImageListFromDatabase()
        initSearchResultsFromDatabase()
    }
    /**
     * Initializes the imageList to hold every image from the database
     */
    fun initImageListFromDatabase()
    {
        viewModelScope.launch(Dispatchers.IO){
            _imageList.postValue(mRepository.getAllImages() as MutableList<ImageData>)
        }
    }

    fun initSearchResultsFromDatabase()
    {
        viewModelScope.launch(Dispatchers.IO){
            _searchResults.postValue(mRepository.getAllImages() as MutableList<ImageData>)
        }
    }

    /**
     * Updates the tripsSearchResults observable LiveData. Finds all trips, then an image associated with a given trip
     */
    fun initTripSearchResultsFromDatabase()
    {
        //We go through all the trips. Then we go through all entries. Then we find some entry that hopefully has an image
        viewModelScope.launch(Dispatchers.IO){
            val allTrips = mRepository.getAllTrips() as MutableList<TripData>
            //Initialize the returnlist
            val returnList =  ArrayList<Pair<TripData,ImageData?>>()

            //Iterate through trips
            for(trip in allTrips)
            {
                val allEntries = mRepository.getEntriesOfTrip(trip)
                //If trip has no entries
                if (allEntries?.size == 0)
                {
                    //Then it certainly has no image
                    returnList.add(Pair(trip,null))
                    continue
                }
                //Otherwise
                var foundImage = false
                //Go through all entries to find an image
                for (entry in allEntries!!)
                {
                    val allImages = mRepository.getImagesOfEntry(entry)
                    //If at least an image is found, add it to the pair
                    if (allImages?.size!! > 0)
                    {

                        returnList.add(Pair(trip,allImages[0]))
                        foundImage = true
                        break
                    }
                }
                //If no image has been found, add a pair with null
                if (foundImage == false)
                    returnList.add(Pair(trip,null))
            }

            _tripsSearchResults.postValue(returnList as MutableList<Pair<TripData,ImageData?>>)
        }
    }

    /**
     * Given a query, it updates the _searchResults livedata
     */
    fun search(query: String?) {
        viewModelScope.launch {
            if (query.isNullOrBlank())
            {
                _searchResults.value = mRepository.getAllImages() as MutableList<ImageData>
            } else
            {
                val sanitizedQuery = sanitizeSearchQuery(query)
                mRepository.search(sanitizedQuery).let {
                    _searchResults.value = it as MutableList<ImageData>
                }
            }
        }
    }

    //REFACTOR this to spawn a coroutine at each iteration, then wait at the end for the results to improve runtime
    //Maybe add this as a function of the Repository?
    /**
     * Internal function that inserts a list of ImageData objects into the database. Updates the list with their generated id's
     * Also associates the imageData with an entry
     */
    private fun insertAndUpdateImageDataList(imageDataList : List<ImageData>)
    {
        for (imageData in imageDataList)
        {
            var id = insertImageReturnId(imageData)
            imageData.id = id
        }
        //Update the observable live data
        initImagesList()
    }
    private fun insertAndUpdateImageDataListWithEntry(imageDataList : List<ImageData>, entryData: EntryData)
    {
        for (imageData in imageDataList)
        {
            imageData.entry_id = entryData.id
            var id = insertImageReturnId(imageData)
            imageData.id = id
        }
        //Update the observable live data
        initImagesList()
    }

    /**
     * Given an imageData, it deletes it from the database and updates the observable liveadata
     */
    fun deleteImageInDatabase(imageData : ImageData)
    {
        viewModelScope.launch {
            mRepository.deleteImage(imageData)
        }
        //Update the livedata
        updateImageList()
    }


    /**
     * Updates the imageList LiveData to reflect what is in the database
     */
    private fun updateImageList()
    {
        viewModelScope.launch{
            _imageList.value = mRepository.getAllImages() as MutableList<ImageData>
        }
    }
    //----------------------- Trip related functionality------------

    /**
     * Insert a trip and return it's generated id
     */
    fun insertTripReturnId(tripData: TripData): Int? = runBlocking{
        var deferredId = async { mRepository.insertTripReturnId(tripData) }
        val id = deferredId.await()
        //Update observable liveData tracking all trips
        initTripSearchResultsFromDatabase()
        id
    }

    /**
     * Insert a trip into the database
     */
    //UNTESTED
    fun insert_trip(tripData: TripData)
    {
         viewModelScope.launch()
         {
             mRepository.insertTrip(tripData)
             //Update observable liveData tracking all trips
             initTripSearchResultsFromDatabase()
         }
    }

    //NOTE: TODO I will refactor all of these and make the function naming convention consistent.
    // I keep using Camel case and underscores in the same API, it's probably frustrating to use
    /**
     * Delete a trip from the database
     */
    fun delete_trip(tripData: TripData)
    {
        viewModelScope.launch()
        {
            mRepository.deleteTrip(tripData)
            //Update observable liveData tracking all trips
            initTripSearchResultsFromDatabase()
        }
    }

    /**
     * Create a new trip based on trip description and inserts it into the database.
     * This returns the triData's id. Because this returns something it blocks the main thread.
     */
    fun create_insert_return_tripID(title :String,country :String, timestamp: Float) : Int
    {
       val createdTrip = TripData(title = title, country = country, trip_timestamp = timestamp)
       //Block the main thread in order to wait for the id to return
       val id : Int? = insertTripReturnId(createdTrip)
       return id!!
    }

    /**
     * Create a new trip based on trip description and inserts it into the database.
     */
    //UNTESTED
    fun create_insert_trip(title :String,country :String, timestamp: Float)
    {
        val createdTrip = TripData(title = title, country = country, trip_timestamp = timestamp)
        viewModelScope.launch()
        {
            mRepository.insertTrip(createdTrip)
        }
    }

    //-----------------------Entry related functionality------------


    /**
     * Updates the observable containing pairs of entry,image
     */
    fun updateLiveDataEntriesOfTrip(tripData : TripData)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val updatedList = ArrayList<Pair<EntryData,List<ImageData>>>()
            //1. get all the entries in one place
            val allEntries = mRepository.getEntriesOfTrip(tripData)
            //2. Iterate through allEntries, and make a pair whenever you find a list, make it null when you don't, you know the drill
            //Think of what happens when there's no entry in the list
            for (entry in allEntries!!)
            {
                val allImages = mRepository.getImagesOfEntry(entry)
                //Now I have all the images for an entry. It could be an empty list
                //I think I just add that?
                updatedList.add(Pair(entry,allImages!!))
            }

            //Update this based on the tripData
            _entriesOfTrip.postValue(updatedList)
        }
    }





    /**
     * Return the images associated with an entry if they exist. Otherwise return an empty list
     * Does block the main thread.
     */
    fun get_entry_images(entryData: EntryData) : List<ImageData>
    {
        var entryImages : ArrayList<ImageData>
        //I need to find all images that point to this entry
        runBlocking {
            entryImages = mRepository.getImagesOfEntry(entryData) as ArrayList<ImageData>
        }
        return entryImages
    }

    /**
     * Given a tripData object and measurements, create and insert an Entry into the database
     */
    fun create_insert_entry(tripData: TripData, temperature:Float?, pressure:Float?, lat:Double, lon:Double, timestamp:Long)
    {
        val createdEntry = EntryData(lat = lat,lon = lon,
            entry_timestamp = timestamp, entry_temperature = temperature,
            entry_pressure = pressure, trip_id = tripData.id)

        insertEntry(createdEntry)
    }
    /**
     * Given a tripData object and measurements, create and insert an Entry into the database. Returns the inserted Entry
     * (including the generated it from inserting)
     */
    fun create_insert_entry_returnEntry(tripData: TripData, temperature:Float?, pressure:Float?, lat:Double, lon:Double, timestamp:Long) : EntryData
    {
        val createdEntry = EntryData(lat = lat,lon = lon,
            entry_timestamp = timestamp, entry_temperature = temperature,
            entry_pressure = pressure, trip_id = tripData.id)

        val id = insertEntryReturnId(createdEntry)
        createdEntry.id = id!!
        return createdEntry
    }

    /**
     * Inserts an entry into the database. Does not return anything. Does not block the main UI thread
     */
    fun insertEntry(entryData: EntryData)
    {
        viewModelScope.launch()
        {
            mRepository.insertEntry(entryData)
        }
    }

    /**
     * Inserts an entry into the database. Returns the id of the entry. Since it returns the id, it blocks the main thread. This could perhaps
     * be avoided with some sort of callback magic?
     */
    fun insertEntryReturnId(entryData: EntryData): Int? = runBlocking{
        var deferredId = async { mRepository.insertEntryReturnId(entryData) }
        deferredId.await()
    }

    //Temporary debug functions

    fun debug_getImages() : List<ImageData>?
    {
        var imageList : List<ImageData>?
        runBlocking {
            imageList = mRepository.getAllImages()
        }
        return imageList
    }
    //Given an tripId return a tripData
    fun debug_returnTripDataOfId(tripId: Int) : TripData?
    {
        val returnTripData : TripData?
        runBlocking(Dispatchers.IO){
            returnTripData =  mRepository.getTrip(tripId)
        }
        return returnTripData
    }

}

