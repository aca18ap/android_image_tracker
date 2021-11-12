package com.com4510.team01.viewModel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.com4510.team01.model.data.Repository
import com.com4510.team01.model.data.database.ImageData
import com.com4510.team01.util.append
import com.com4510.team01.util.convertToImageDataWithoutId
import kotlinx.coroutines.*
import pl.aprilapps.easyphotopicker.MediaFile

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)

    /**
     * Observable list of images. Contains all images a user has taken or added to the Travel app represented as ImageData.
     */
    private val imageList: MutableLiveData<MutableList<ImageData>> =
        MutableLiveData<MutableList<ImageData>>().also{
            initImagesFromDatabase()
    }
    fun getImageList(): LiveData<MutableList<ImageData>> {
        return imageList
    }

    // To do: Find a way not to block the ui thread here. Worst case scenario provide a function that inserts multiple ImageData's at a time each on its own coroutine
    /**
     * Inserts an imageData into the database and returns the id it was associated with. Warning: This does block the UI thread.
     */
    fun insertDataReturnId(imageData: ImageData): Int = runBlocking{
        var deferredId = async{ mRepository.insertDataReturnId(imageData)}
        deferredId.await()
    }

    /**
     * Handles the photos returned by EasyImage.
     */
    fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        var imageDataList = returnedPhotos.convertToImageDataWithoutId()
        insertAndUpdateImageDataList(imageDataList)
        addToObservableImageList(imageDataList)
    }

    /**
     * Returns the list of images as a List, or, if there are no Images, returns an empty list of images
     */
    fun getImagesAsList():List<ImageData>
    {
        return imageList.value ?: ArrayList<ImageData>()
    }

    /**
     * Function that initializes the imageList. Only gets called once, the first time imageList is used.
     */
    private fun initImagesFromDatabase()
    {
        viewModelScope.launch{
            mRepository.getAllImages()?.let { imageList.append(it) }
            Log.d("DebugViewModel","LoadedImages:" + imageList.value.toString())
        }
    }
    /**
     * Internal function to add images to the imageList(No persistence)
     */
    private fun addToObservableImageList(list : List<ImageData>)
    {
        imageList.append(list)
    }

    //REFACTOR this to spawn a coroutine at each iteration, then wait at the end for the results to improve runtime
    //Maybe add this as a function of the Repository?
    /**
     * Inserts a list of ImageData objects into the database. Updates the list with their generated id's
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


