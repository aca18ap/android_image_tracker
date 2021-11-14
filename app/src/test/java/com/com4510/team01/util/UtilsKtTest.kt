package com.com4510.team01.util


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.com4510.team01.model.data.database.ImageData
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import pl.aprilapps.easyphotopicker.MediaFile
import java.io.File

class UtilsKtTest : TestCase() {
    lateinit var arrayMediaFile : Array<MediaFile>
    lateinit var listToPassToLiveData : MutableList<ImageData>
    lateinit var liveDataOfImageList : MutableLiveData<MutableList<ImageData>>
    lateinit var listImageData : List<ImageData>


    override fun setUp()
    {
        //Setup the arrayMediaFiles
        arrayMediaFile = arrayOf<MediaFile>(
            MediaFile(mock(Uri::class.java),mock(File::class.java)),
            MediaFile(mock(Uri::class.java),mock(File::class.java)),
            MediaFile(mock(Uri::class.java),mock(File::class.java)),
            MediaFile(mock(Uri::class.java),mock(File::class.java))
        )

        `when`(arrayMediaFile.get(0).file.name).        thenReturn("MediaFile0")
        `when`(arrayMediaFile.get(0).file.absolutePath).thenReturn("MediaFile0Path")

        `when`(arrayMediaFile.get(1).file.name).        thenReturn("MediaFile1")
        `when`(arrayMediaFile.get(1).file.absolutePath).thenReturn("MediaFile1Path")

        `when`(arrayMediaFile.get(2).file.name).        thenReturn("MediaFile2")
        `when`(arrayMediaFile.get(2).file.absolutePath).thenReturn("MediaFile2Path")

        `when`(arrayMediaFile.get(3).file.name).        thenReturn("MediaFile3")
        `when`(arrayMediaFile.get(3).file.absolutePath).thenReturn("MediaFile3Path")

        //Setup the livedata containing the list of ImageData

        listToPassToLiveData = (MutableList(4){
            ImageData(it, "Path$it","ImageDataTitle$it","ImageDataDescription$it","ImageDataThumbnailUrl$it")
        })
        //ServiceCall<SurveyListModel>::class.java
        liveDataOfImageList = mock(MutableLiveData::class.java) as MutableLiveData<MutableList<ImageData>>
        `when`(liveDataOfImageList.value).thenReturn(listToPassToLiveData)


        listImageData = MutableList(4){
            val biggerIt = it + 6
            ImageData(it, "Path$biggerIt","ImageDataTitle$biggerIt","ImageDataDescription$biggerIt","ImageDataThumbnailUrl$biggerIt")
        }

    }

    @Test
    fun testConvertToImageDataWithoutIdSize() {
        var listImageData = arrayMediaFile.convertToImageDataWithoutId()

        assertEquals(4,listImageData.size)
    }

    @Test
    fun testConvertToImageDataWithoutIdTitleAndUri() {
        var listImageData = arrayMediaFile.convertToImageDataWithoutId()

        //Names
        assertEquals(arrayMediaFile.get(0).file.name,listImageData.get(0).imageTitle)
        assertEquals(arrayMediaFile.get(1).file.name,listImageData.get(1).imageTitle)
        assertEquals(arrayMediaFile.get(2).file.name,listImageData.get(2).imageTitle)
        assertEquals(arrayMediaFile.get(3).file.name,listImageData.get(3).imageTitle)

        //Uri's
        assertEquals(arrayMediaFile.get(0).file.absolutePath,listImageData.get(0).imageUri)
        assertEquals(arrayMediaFile.get(1).file.absolutePath,listImageData.get(1).imageUri)
        assertEquals(arrayMediaFile.get(2).file.absolutePath,listImageData.get(2).imageUri)
        assertEquals(arrayMediaFile.get(3).file.absolutePath,listImageData.get(3).imageUri)

    }
    @Test
    fun testConvertToImageDataEmptyMediaFileList() {
        var emptyMediaFileArray = arrayOf<MediaFile>()
        var listImageData = emptyMediaFileArray.convertToImageDataWithoutId()

        assertEquals(0,listImageData.size)

    }
    @Test
    fun testLiveDataAppendEmpty()
    {
        val emptyImageDataList = arrayListOf<ImageData>()
        liveDataOfImageList.append(emptyImageDataList)
        assertEquals(liveDataOfImageList.value?.size,4)
        assertEquals(liveDataOfImageList.value,liveDataOfImageList.value)
    }
    @Test
    fun testLiveDataAppendCount()
    {
        liveDataOfImageList.append(listImageData)
        assertEquals(8,liveDataOfImageList.value?.size)
    }
    @Test
    fun testLiveDataAppend()
    {
        liveDataOfImageList.append(listImageData)
        val expectedListValue = listToPassToLiveData as ArrayList<ImageData>
        expectedListValue.addAll(listImageData)
        assertEquals(expectedListValue,liveDataOfImageList.value)
    }


}