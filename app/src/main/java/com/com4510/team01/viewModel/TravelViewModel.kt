package com.com4510.team01.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.com4510.team01.ImageApplication
import com.com4510.team01.model.data.Repository
import com.com4510.team01.model.data.database.ImageData
import kotlinx.coroutines.*

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)

    /**
     * Observable list of images. Contains all images a user has taken or added to the Travel app represented as ImageData.
     */
    private val imageList: MutableLiveData<MutableList<ImageData>> by lazy {
        MutableLiveData<MutableList<ImageData>>().also{
            initImagesFromDatabase()
        }
    }
    fun getImages(): LiveData<MutableList<ImageData>> {
        return imageList
    }

    /**
     * Returns the list of images as a List, or, if there are no Images, returns an empty list of images
     */
    fun getImagesAsList():List<ImageData>
    {
        return imageList.value ?: ArrayList<ImageData>()
    }

    /**
     * Add images to the imageList(No persistance) To possibly remove
     */
    fun addImages(list : List<ImageData>)
    {
        imageList.append(list)
    }
    /**
     * Function that initializes the imageList. Only gets called once, the first time imageList is used.
     */
    private fun initImagesFromDatabase()
    {
        viewModelScope.launch{
            //load data into the imageList
            //IFBROKEN: Look here, this is a weird ass lookin statement

            mRepository.getAllImages()?.let { imageList.append(it) }
            Log.d("DebugViewModel","Loaded all images, in theory")
        }
    }

    // To do: Find a way not to block the ui thread here. Worst case scenario provide a function that inserst multiple ImageData's at a time each on its own coroutine
    /**
     * Inserts an imageData into the database and returns the id it was associated with. Warning: This does block the UI thread.
     */
    fun insertDataReturnId(imageData: ImageData): Int = runBlocking{
            var deferredId = async{ mRepository.insertDataReturnId(imageData)}
            deferredId.await()
    }
}


fun  MutableLiveData<MutableList<ImageData>>.append(list: List<ImageData>) {
    val value = this.value ?: arrayListOf()
    value.addAll(list)
    this.value = value
}



