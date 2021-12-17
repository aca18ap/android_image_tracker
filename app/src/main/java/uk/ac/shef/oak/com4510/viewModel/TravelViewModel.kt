package uk.ac.shef.oak.com4510.viewModel


import android.app.Application
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
     * Observable list of trips that can be used with searching
     */
    private val _tripsSearchResults = MutableLiveData<MutableList<TripData>>()
    val tripsSearchResults : LiveData<MutableList<TripData>>
        get() = _tripsSearchResults




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
    fun insertArrayMediaFiles(mediaFileArray: Array<MediaFile>) {
        var imageDataList = mediaFileArray.convertToImageDataWithoutId()
        insertAndUpdateImageDataList(imageDataList)
        _imageList.append(imageDataList)
    }

    /**NOT TESTED BUT PROBABLY WORKS
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

    fun initTripsList()
    {
        initTripSearchResultsFromDatabase()
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
        viewModelScope.launch{
            _imageList.value = mRepository.getAllImages() as MutableList<ImageData>
        }
    }

    fun initSearchResultsFromDatabase()
    {
        viewModelScope.launch{
            _searchResults.value = mRepository.getAllImages() as MutableList<ImageData>
        }
    }



    fun initTripSearchResultsFromDatabase()
    {
        viewModelScope.launch{
            _tripsSearchResults.value = mRepository.getAllTrips() as MutableList<TripData>
        }
    }

    /*
    fun tripSearch(query: String?){
        viewModelScope.launch{
            if(query.isNullOrBlank())
            {
                _tripsSearchResults.value = mRepository.getAllTrips() as MutableList<TripData>
            }else{
                val sanitizedQuery = sanitizeSearchQuery(query)
                mRepository.search(sanitizedQuery).let {
                    _tripsSearchResults.value = it as MutableList<TripData>
                }
            }
        }
    }
    */



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
    // Trip related functionality

    fun insertTripReturnId(tripData: TripData): Int? = runBlocking{
        var deferredId = async { mRepository.insertTripReturnId(tripData) }
        deferredId.await()
    }


    // Entry related functionality

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

}

