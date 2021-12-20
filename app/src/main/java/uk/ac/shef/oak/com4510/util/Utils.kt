package uk.ac.shef.oak.com4510.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import pl.aprilapps.easyphotopicker.MediaFile
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter

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


/**
 * helper function to generate a bitmap object of a given size from an image's file path.
 */
fun decodeSampledBitmapFromResource(
    filePath: String,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()

    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(filePath, options);
}

/**
 * helper function to calculate an inSampleSize for resampling to achieve the right picture
 * density for a smaller size file.
 * See inSampleFile: https://developer.android.com/reference/android/graphics/BitmapFactory.Options#inSampleSize
 */
fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    // Raw height and width of image
    val height = options.outHeight;
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = (height / 2).toInt()
        val halfWidth = (width / 2).toInt()

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= reqHeight
            && (halfWidth / inSampleSize) >= reqWidth
        ) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize.toInt();
}