@file:Suppress("MemberVisibilityCanBePrivate")

package uk.ac.shef.oak.com4510.viewModel


import android.app.Application
import android.util.Log
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
import uk.ac.shef.oak.com4510.util.convertToImageDataWithoutId
import uk.ac.shef.oak.com4510.util.sanitizeSearchQuery

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)
    init {
        //Initializes the searchResults liveData to initially contain all images from the database
        initSearchResults()
        //Initializes the allTrips observable to contain all the trips in the database
        updateAllTripsObservable()
    }

    //Separate constructor to allow passing a different repository. For testing
    constructor(repository: Repository, app : Application) : this(app) {
        mRepository = repository
    }

    val someRandomINTHALL = 5

    private val _onGoingTrip  = MutableLiveData(-1)
    /**
     * Observable int that contains the ID of an ongoing trip.
     *
     * @see setOnGoingTrip
     * @see getOnGoingTrip
     */
    val onGoingTrip : LiveData<Int>
        get() = _onGoingTrip

    fun setOnGoingTrip(tripID: Int)
    {
        _onGoingTrip.value = tripID
        Log.d("TravelViewModel", "onGoingTrip variable:${_onGoingTrip.value}")
    }

    fun getOnGoingTrip() : Int
    {
        return _onGoingTrip.value!!
    }

    private val _searchResults  = MutableLiveData<MutableList<ImageData>>()
    /**
     * Observable list of images that contains the result of a search performed on a given query.
     *
     * @see search
     */
    val searchResults : LiveData<MutableList<ImageData>>
        get() = _searchResults

    /**
     * Given a query, it updates the _searchResults livedata with the all the
     * images whose description or title match the query.
     *
     * @param query Query string the search is made against. Any ImageData whose description
     * or title match [query] would be returned by the search.
     */
    fun search(query: String?, order : OrderBy) {
        viewModelScope.launch {
            if (query.isNullOrBlank())
            {
                _searchResults.postValue(mRepository.getAllImages(order) as MutableList<ImageData>)
            } else
            {
                val sanitizedQuery = sanitizeSearchQuery(query)
                mRepository.search(sanitizedQuery,order).let {
                    _searchResults.postValue(it as MutableList<ImageData>)
                }
            }
        }
    }

    /**
     * Initializes the _searchResults LiveData to contain every image from the database.
     */
    private fun initSearchResults()
    {
        Log.d("TravelViewModel", "LastGuyWhoLaunched the coroutine in initSearchResults",Exception("ShowMeStackTrace"))
        viewModelScope.launch(Dispatchers.IO){
            val allImages = mRepository.getAllImages(OrderBy.NOPARTICULARORDER) ?: ArrayList<ImageData>()
            Log.d("TravelViewModel", "This is what is in allImages:${allImages.toString()}")
            Log.d("TravelViewModel", "SearchResults variable:${_searchResults.toString()}")
            if(isActive) {
                _searchResults.postValue(allImages as MutableList<ImageData>)
            }
        }
    }

    private val _allTripsObservable = MutableLiveData<MutableList<Pair<TripData,ImageData?>>>()
    /**
     * Observable list of pair<TripData,ImageData?>. Kept updated with all the trips in the Database
     *
     * @see updateAllTripsObservable
     */
    val allTripsObservable : LiveData<MutableList<Pair<TripData,ImageData?>>>
        get() = _allTripsObservable

    /**
     * Updates allTripsObservable to contain a pair containing all the trips in the database, along with a random single image associated with each trip.
     */
    fun updateAllTripsObservable()
    {
        //We go through all the trips. Then we go through all entries. Then we find some entry that hopefully has an image
        viewModelScope.launch(Dispatchers.IO){
            val allTrips = mRepository.getAllTrips() as MutableList<TripData>
            //Initialize the returnlist
            val returnList =  ArrayList<Pair<TripData,ImageData?>>()

            //Iterate through trips
            for(trip in allTrips)
            {
                val allEntries = mRepository.getEntriesOfTrip(trip.id)
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
                    val allImages = mRepository.getImagesOfEntry(entry.id)
                    //If at least an image is found, add it to the pair
                    if (allImages?.size!! > 0)
                    {

                        returnList.add(Pair(trip,allImages[0]))
                        foundImage = true
                        break
                    }
                }
                //If no image has been found, add a pair with null
                if (!foundImage)
                    returnList.add(Pair(trip,null))
            }
            _allTripsObservable.postValue(returnList as MutableList<Pair<TripData,ImageData?>>)
        }
    }

    private val _entriesOfTrip = MutableLiveData<MutableList<Pair<EntryData,List<ImageData>>>>()
    /**
     * Observable list of all (EntryData,List<ImageData>) corresponding to a particular trip. The List<ImageData> represents a
     * list of all images associated with each entry.
     *
     * @see updateEntriesOfTrip
     */
    val entriesOfTrip : LiveData<MutableList<Pair<EntryData,List<ImageData>>>>
        get() = _entriesOfTrip
    // This value holds the last entry that entriesOfTrip was used with. This is so that it is possible to update
    // the entriesOfTrip observable whenever any updates regarding trips happens in the database.
    private lateinit var currentEntryForEntriesOfTrip : EntryData

    /**
     * Updates the entriesOfTrip observable with all (DataEntry,List<ImageData>) for a given trip.
     * @param tripDataID Id of the trip we're retrieving the entries for.
     */
    fun updateEntriesOfTrip(tripDataID : Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val updatedList = ArrayList<Pair<EntryData,List<ImageData>>>()
            val allEntries = mRepository.getEntriesOfTrip(tripDataID)
            for (entry in allEntries!!)
            {
                val allImages = mRepository.getImagesOfEntry(entry.id)
                //Now I have all the images for an entry. It could be an empty list
                updatedList.add(Pair(entry,allImages!!))
            }
            _entriesOfTrip.postValue(updatedList)
        }
    }

    private val _imagesOfEntry = MutableLiveData<MutableList<ImageData>>()
    /**
     * Observable list of images that contains images for a particular entry.
     *
     * @see updateImagesOfEntry
     */
    val imagesOfEntry : LiveData<MutableList<ImageData>>
        get() = _imagesOfEntry

    /**
     * Updates the imagesOfEntry observable LiveData to contain all images of an entry.
     * @param entryDataID Id of the entry whose associated images are going to be reflected by the imagesOfEntry observable
     */
    fun updateImagesOfEntry(entryDataID: Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            //Get all images for a given entry
            val allImages = mRepository.getImagesOfEntry(entryDataID)
            _imagesOfEntry.postValue(allImages as MutableList<ImageData>?)
        }
    }

    private val _imagesOfTrip = MutableLiveData<MutableList<ImageData>>()
    /**
     * Observable liveData containing all images for a given trip.
     *
     * @see updateImagesOfTrip
     */
    val imagesOfTrip : LiveData<MutableList<ImageData>>
        get() = _imagesOfTrip

    /**
     * Updates the imagesOfTrip observable liveData to contain all images of a given trip
     * @param tripDataID Id of the trip whose associated images are going to be reflected by the imagesOfTrip observable
     */
    fun updateImagesOfTrip(tripDataID: Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {

            val allImages = ArrayList<ImageData>()
            //We have a trip, we can get to all the entries pointing to that trip

            val allEntries = mRepository.getEntriesOfTrip(tripDataID)


            //We have entries, we can get to all the images pointing to those entries
            //If the trip has any entries, collect images in allImages
            if (allEntries!!.isNotEmpty()) {
                for (entry in allEntries) {
                    //For each entry we can get its individual images
                    val imagesOfEntry = mRepository.getImagesOfEntry(entry.id)
                    allImages.addAll(imagesOfEntry!!)
                }
            }
            //Otherwise update _imageOfTrip with an empty list
            _imagesOfTrip.postValue(allImages)
        }
    }




    /**
     * Returns an ImageData object with the given id
     * @param imageID ID of the image to get
     * @return ImagaData of the image
     */
    fun getImage(imageID : Int) : ImageData? = runBlocking(Dispatchers.IO) {
        mRepository.getImage(imageID)
    }

    /**
     * Inserts an imageData into the database and returns the id it was associated with.
     * Warning: This does block the UI thread, it is meant to be used sparingly, if at all.
     *
     * @param  imageData  ImageData to be inserted
     * @return id of the inserted image
     */
    fun insertImageReturnId(imageData: ImageData): Int = runBlocking{
        val deferredId = async { mRepository.insertImageReturnId(imageData) }
        val id = deferredId.await()
        //update the searchResults observable to match what is inside the database
        initSearchResults()
        id
    }

    /**
     * Inserts an imageData into the database.
     *
     * @param imageData ImageData to be inserted
     */
    private fun insertImage(imageData: ImageData)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            mRepository.insertImageReturnId(imageData)

            //update the searchResults observable to match what is inside the database
            initSearchResults()
        }
    }
    /**
     * Inserts a list of imageData into the database.
     *
     * @param  listImageData  List of ImageData to be inserted
     */
    private fun insertImageList(listImageData : List<ImageData>)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            mRepository.insertListImageData(listImageData)

            //update the searchResults observable to match what is inside the database
            initSearchResults()
        }
    }

    /**
     * Debug function used to add images to the database without having them be associated with any entry.
     *
     * Meant to be used with the easyImage library. An array of Mediafiles is made available by the EasyImage library as an argument
     * in the onMediaFilesPicked callback function used when handling the relevant ActivityResult.
     * The MediaFile objects in this array are converted to ImageData objects and inserted into the database.
     *
     * @param mediaFileArray Array made available by the EasyImage library.
     * in the onMediaFilesPicked callback function used when handling the relevant ActivityResult
     */
    fun debug_insertArrayMediaFiles(mediaFileArray: Array<MediaFile>) {
        val imageDataList = mediaFileArray.convertToImageDataWithoutId()
        insertImageList(imageDataList)
    }

    /**
     * Meant to be used with the easyImage library. An array of Mediafiles is made available by the EasyImage library as an argument
     * in the onMediaFilesPicked callback function used when handling the relevant ActivityResult.
     * The MediaFile objects in this array are converted to ImageData objects, inserted into the database and associated with the given
     * entry.
     *
     * @param  mediaFileArray  Array of mediafiles returned by the easyImage library when a user selects images from the gallery and submits them
     * , or takes a photo and submits it.
     * @param  entryDataID Entry object to link the array of image to
     */
    fun insertArrayMediaFilesWithEntry(mediaFileArray: Array<MediaFile>, entryDataID : Int) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val imageDataList = mediaFileArray.convertToImageDataWithoutId()
            insertImageListWithEntry(imageDataList, entryDataID)
        }
    }

    /**
     * Meant to be used with easyImage. Equivalent to passing the last entry by id to [insertArrayMediaFilesWithEntry].
     *
     * Given an array of Mediafile images returned by easyImage, it converts the MediaFiles
     * into ImageData, inserts them into the database and links them with the last entry in the database by id.
     *
     * @param  mediaFileArray  Array of mediafiles returned by the easyImage library when a user selects images from the gallery and submits
     * them or takes a photo and submits it.
     */
    fun insertArrayMediaFilesWithLastEntryById(mediaFileArray: Array<MediaFile>)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val lastEntry = mRepository.getLastEntryById()
            if (lastEntry != null) {
                insertArrayMediaFilesWithEntry(mediaFileArray,lastEntry.id)
            }
            else
            {
                Log.e("TravelViewModel","You attempted to insert images before any entry has been created")
            }
        }
    }

    /**
     * Links an ImageData to an EntryData. i.e The imageData's entry_id
     * is made equal to the id of the EntryData argument
     *
     * @param  imageData ImageData to be linked to EntryData
     * @param  entryData EntryData to be linked to ImageData
     */
    fun linkImageDataWithEntry(imageData: ImageData,entryData: EntryData)
    {
        imageData.entry_id = entryData.id
        viewModelScope.launch(Dispatchers.IO){
            mRepository.updateImage(imageData)

            //Update the allTripsObservable. Now that a new entry has an image, a new trip might have an image that it didn't have before.
            updateAllTripsObservable()
        }
    }

    /**
     * Given an ImageData and ImageData fields, updates the corresponding ImageData with the fields inside the database.
     * Any of the imageData fields could be omitted.
     *
     * @param  imageData  imageData to update.
     * @param  title title of ImageData to update to
     * @param  description description of ImageData to update to
     * @param  entry_id entry_id of ImageData to update to
     */
    fun updateImageInDatabase(imageData : ImageData, title : String? = null, description : String? = null,entry_id : Int? = null)
    {
        viewModelScope.launch {
            val updatedImage = ImageData(imageData.id,
                imageData.imageUri,
                title ?: imageData.imageTitle,
                description ?: imageData.imageDescription,
                imageData.thumbnailUri,
                entry_id ?: imageData.entry_id)
            updatedImage.thumbnail = imageData.thumbnail
            mRepository.updateImage(updatedImage)

            //update the searchResults observable to match what is inside the database
            initSearchResults()

            //If this image was associated with a new entry, update the allTripsObservable.
            // Since now that a new entry has an image, a new trip might have an image that it didn't have before.
            if (entry_id != null)
                updateAllTripsObservable()
        }
    }


    /**
     * Inserts a list of ImageData objects into the database and
     * associates each imageData in the list with a specified entry
     *
     * @param  imageDataList  List of ImageData objects to insert.
     * @param  entryDataID Entry to associate the images with
     */
    private fun insertImageListWithEntry(imageDataList : List<ImageData>, entryDataID: Int)
    {
        for (imageData in imageDataList)
        {
            imageData.entry_id = entryDataID
        }
        insertImageList(imageDataList)
    }

    /**
     * Deletes an imageData from the database.
     * @param  imageData  ImageData to delete from the list
     */
    fun deleteImageFromDatabase(imageData : ImageData)
    {
        viewModelScope.launch {
            mRepository.deleteImage(imageData)

            //update the searchResults observable to match what is inside the database
            initSearchResults()
        }
    }

    //----------------------- Trip related functionality----------------------------

    /**
     * Returns a trip given its id
     * @param tripID Id of tripData to return
     */
    fun getTrip(tripID : Int) = runBlocking{
        mRepository.getTrip(tripID)
    }

    /**
     * If a trip has entries associated with it return true otherwise false.
     * Warning: Blocks the ui thread. Use sparingly
     *
     * @param tripDataID Id of the trip
     * @return Boolean that represents whether the trip corresponding to [tripDataID] has any entries associated with it
     */
    fun tripHasEntries(tripDataID: Int) : Boolean = runBlocking(Dispatchers.IO){
        var triphasentries = true

        if (mRepository.getEntriesOfTrip(tripDataID)?.isEmpty() == true)
            triphasentries = false

        triphasentries
    }

    /**
     * Insert a trip and return its generated id
     * Warning: This does block the UI thread. Use sparingly
     *
     * @param tripData Trip to insert
     * @return id of the inserted TripData generated by the database
     */
    fun insertTripReturnId(tripData: TripData): Int? = runBlocking{
        val deferredId = async { mRepository.insertTripReturnId(tripData) }
        val id = deferredId.await()
        //Update observable liveData tracking all trips
        updateAllTripsObservable()
        id
    }

    /**
     * Delete a trip from the database
     *
     * @param tripData Trip to delete
     */
    fun deleteTrip(tripData: TripData)
    {
        viewModelScope.launch()
        {
            mRepository.deleteTrip(tripData)
            //Update observable liveData tracking all trips
            updateAllTripsObservable()
        }
    }

    /**
     * Create a new trip based on trip attributes and inserts it into the database.
     * Returns the tripData's id.
     * Warning: This does block the UI thread, it is meant for light use.
     *
     * @param title Title of the trip
     * @param country Name of the trip country
     * @param timestamp Time at which the trip was started
     * @return Created trip's id
     */
    fun create_insert_return_tripID(title :String,country :String, timestamp: Float) : Int
    {
       val createdTrip = TripData(title = title, country = country, trip_timestamp = timestamp)
       //Block the main thread in order to wait for the id to return
       val id : Int? = insertTripReturnId(createdTrip)
       return id!!
    }

    /**
     * Same as [create_insert_return_tripID] but does not return the trip ID.
     * Because of that, it does not need to block the UI thread
     *
     * Create a new trip based on trip attributes and inserts it into the database.
     * This does block the main UI thread.
     *
     * @param title Title of the trip
     * @param country Name of the trip country
     * @param timestamp Time at which the trip was started
     */
    fun create_insert_trip(title :String,country :String, timestamp: Float)
    {
        val createdTrip = TripData(title = title, country = country, trip_timestamp = timestamp)
        viewModelScope.launch()
        {
            mRepository.insertTripReturnId(createdTrip)

            //Update observable liveData tracking all trips
            updateAllTripsObservable()
        }
    }

    //-----------------------Entry related functionality-------------------------

    /**
     * Deletes an entry from the database
     * @param entryData EntryData to delete
     */
    fun deleteEntry(entryData: EntryData)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            mRepository.deleteEntry(entryData)
        }
        //Update observable liveData tracking all trips
        updateAllTripsObservable()
    }
    /**
     * Returns an entry with the given id if it exists in the database, null otherwise.
     * Warning: This does block the UI thread
     *
     * @param entryId Id of the entry to be retrieved
     * @return Entry with the id specified if it exists in the database, null otherwise
     */
    fun getEntry(entryId : Int) : EntryData? = runBlocking{
        mRepository.getEntry(entryId)
    }

    // -------------------------- DEBUG FUNCTIONS
    fun debug_getImages() : List<ImageData>?
    {
        var imageList : List<ImageData>?
        runBlocking {
            imageList = mRepository.getAllImages(OrderBy.NOPARTICULARORDER)
        }
        return imageList
    }

}

