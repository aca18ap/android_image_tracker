package com.com4510.team01.util

import androidx.lifecycle.MutableLiveData
import com.com4510.team01.model.data.database.ImageData
import pl.aprilapps.easyphotopicker.MediaFile

/**
 * Contains various utility functions and extension functions
 */

/**
 * given a list of photos, it returns a list of ImageData objects. They don't yet have id's
 */
fun Array<MediaFile>.convertToImageDataWithoutId(): List<ImageData> {
    val imageDataList: MutableList<ImageData> = java.util.ArrayList<ImageData>()
    for (mediaFile in this) {
        val fileNameAsTempTitle = mediaFile.file.name
        var imageData = ImageData(
            imageTitle = fileNameAsTempTitle,
            imageUri = mediaFile.file.absolutePath
        )
        // Update the database with the newly created object
        //var id = insertDataReturnId(imageData)
        //imageData.id = id
        imageDataList.add(imageData)
    }
    return imageDataList
}

/**
 * Appends a list of images to a MutableLiveData list of images
 */
fun MutableLiveData<MutableList<ImageData>>.append(list: List<ImageData>) {
    val value = this.value ?: arrayListOf()
    value.addAll(list)
    this.value = value
}
