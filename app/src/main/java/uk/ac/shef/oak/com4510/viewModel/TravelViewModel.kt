package uk.ac.shef.oak.com4510.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import uk.ac.shef.oak.com4510.model.data.Repository
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.util.append
import uk.ac.shef.oak.com4510.util.convertToImageDataWithoutId
import uk.ac.shef.oak.com4510.util.sanitizeSearchQuery
import kotlinx.coroutines.*
import pl.aprilapps.easyphotopicker.MediaFile

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)

    //Separate constructor to allow passing a different repository. For testing code
    constructor(repository: Repository, app : Application) : this(app) {
        mRepository = repository
    }

    /**
     * Observable list of images. Contains all images in the database
     */
    private val _imageList: MutableLiveData<MutableList<ImageData>> = MutableLiveData<MutableList<ImageData>>()
    val imageList : LiveData<MutableList<ImageData>>
        get() = _imageList

    /**
     * Observable list of images to be used with searching. The search function given a string updates this livedata with
     * a list of all ImageData that contains one of the keywords of the string in either the Title or its Description
     * */
    private val _searchResults  = MutableLiveData<MutableList<ImageData>>()
    val searchResults : LiveData<MutableList<ImageData>>
        get() = _searchResults

    // To do: Find a way not to block the ui thread here. Worst case scenario provide a function that inserts multiple ImageData's at a time each on its own coroutine
    /**
     * Inserts an imageData into the database and returns the id it was associated with. Warning: This does block the UI thread.
     * This does not update any liveData object
     */
    fun insertDataReturnId(imageData: ImageData): Int = runBlocking{
        var deferredId = async { mRepository.insertDataReturnId(imageData) }
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

    /**
     * Updates an imageData in the database. Does not update the observable LiveData TO ADD: Functionality for updating position
     */
    fun updateImageInDatabase(imageData : ImageData, title : String? = null, description : String? = null)
    {
        var updatedImage = ImageData(imageData.id,
            imageData.imageUri,
            title ?: imageData.imageTitle,
            description ?: imageData.imageDescription,
            imageData.thumbnailUri,
            imageData.position)
        updatedImage.thumbnail = imageData.thumbnail
        viewModelScope.launch {
            mRepository.updateImage(updatedImage)
        }
    }

    /**
     * Given an imageData, it deletes it from the database and updates the observable liveadata
     */
    fun deleteImageInDatabase(imageData : ImageData)
    {
        viewModelScope.launch {
            mRepository.delete(imageData)
            //Update the livedata
            updateImageList()
        }

    }


    /**
     * Initialize all observable LiveData
     */
    fun initObservable()
    {
        updateImageList()
        initSearchResults()
    }
    /**
     * Updates the imageList LiveData to reflect what is in the database
     */
    fun updateImageList()
    {
        viewModelScope.launch{
            _imageList.value = mRepository.getAllImages() as MutableList<ImageData>
        }
    }

    /**
     * Initializes the searchResults LiveData to hold every image from the database
     */
    fun initSearchResults()
    {
        viewModelScope.launch{
            _searchResults.value = mRepository.getAllImages() as MutableList<ImageData>
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
     */
    private fun insertAndUpdateImageDataList(imageDataList : List<ImageData>)
    {
        for (imageData in imageDataList)
        {
            var id = insertDataReturnId(imageData)
            imageData.id = id
        }
    }

}


