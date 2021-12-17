package uk.ac.shef.oak.com4510.util

import androidx.lifecycle.MutableLiveData
import uk.ac.shef.oak.com4510.model.data.database.ImageData
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
    this.postValue(value)
}
/**
 * Sanitizes a string to pass to search.
 */
fun sanitizeSearchQuery(query: String?): String {
    if (query == null) {
        return "";
    }
    val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
    return "*\"$queryWithEscapedQuotes\"*"
}
