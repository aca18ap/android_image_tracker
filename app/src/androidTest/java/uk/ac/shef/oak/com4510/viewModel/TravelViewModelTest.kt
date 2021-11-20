package uk.ac.shef.oak.com4510.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import uk.ac.shef.oak.com4510.model.data.Repository
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.model.data.database.ImageRoomDatabase
<<<<<<< HEAD:app/src/androidTest/java/uk/ac/shef/oak/com4510/viewModel/TravelViewModelTest.kt
import com.getOrAwaitValue
=======
import uk.getOrAwaitValue
>>>>>>> Member1PrivateWork:app/src/androidTest/java/com/com4510/team01/viewModel/TravelViewModelTest.kt
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import pl.aprilapps.easyphotopicker.MediaFile
import java.io.File

@RunWith(AndroidJUnit4::class)
class TravelViewModelTest : TestCase() {
    private lateinit var viewModel : TravelViewModel
    private lateinit var listOfImageData : List<ImageData>

    private lateinit var arrayMediaFile : Array<MediaFile>


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // This is happening for every test, so we're setting up a different database for each individual test case
    // Also, the database is being built in volatile memory, not persistent storage as inMemoryDatabaseBuilder is used
    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, ImageRoomDatabase::class.java)
            .allowMainThreadQueries().build()
        val testRepository = Repository(db.imageDataDao(),context as Application)
        viewModel = TravelViewModel(testRepository,context)

        //Make 5 imageData

        listOfImageData = MutableList(4){
            ImageData(it + 1, "Path$it","ImageDataTitle$it","ImageDataDescription$it","ImageDataThumbnailUrl$it")
        }

        viewModel.insertDataReturnId(listOfImageData.get(0))
        viewModel.insertDataReturnId(listOfImageData.get(1))
        viewModel.insertDataReturnId(listOfImageData.get(2))
        viewModel.insertDataReturnId(listOfImageData.get(3))

        setupArrayMediaFlies()
    }

    @Test
    fun testInitData()
    {

        viewModel.updateImageList()
        val returnedImageList = viewModel.imageList.getOrAwaitValue()
        //Check that the livedata update is correct


        assertEquals(returnedImageList.size, listOfImageData.size)
        val areEqual = returnedImageList.zip(listOfImageData).all { (x, y) -> x == y }
        assertTrue(areEqual)

    }

    @Test
    fun insertDataReturnId() {

        val id = viewModel.insertDataReturnId(
            ImageData(
            54, "Path54","ImageDataTitle54","ImageDataDescription54","ImageDataThumbnailUrl54")
        )

        //Test that it's inserted into the database
        viewModel.updateImageList()
        val returnedImageList = viewModel.imageList.getOrAwaitValue()

        val insertedImageExtractFromDatabase = returnedImageList.find{
            it == ImageData(
                54, "Path54","ImageDataTitle54","ImageDataDescription54","ImageDataThumbnailUrl54")
        }
        assertTrue(insertedImageExtractFromDatabase != null)
    }

    @Test
    fun insertArrayMediaFiles() {
        viewModel.insertArrayMediaFiles(arrayMediaFile)
        val allImagesReturnedFromLiveData = viewModel.imageList.getOrAwaitValue()
        val expectedImages = MutableList(4){
                ImageData(it + 5, "MediaFile$it"+"Path","MediaFile$it",null,null)
            }
        assertEquals(expectedImages,allImagesReturnedFromLiveData)

    }


    private fun setupArrayMediaFlies()
    {
        //Setup the arrayMediaFiles
        arrayMediaFile = arrayOf<MediaFile>(
            MediaFile(Mockito.mock(Uri::class.java), Mockito.mock(File::class.java)),
            MediaFile(Mockito.mock(Uri::class.java), Mockito.mock(File::class.java)),
            MediaFile(Mockito.mock(Uri::class.java), Mockito.mock(File::class.java)),
            MediaFile(Mockito.mock(Uri::class.java), Mockito.mock(File::class.java))
        )

        Mockito.`when`(arrayMediaFile.get(0).file.name).        thenReturn("MediaFile0")
        Mockito.`when`(arrayMediaFile.get(0).file.absolutePath).thenReturn("MediaFile0Path")

        Mockito.`when`(arrayMediaFile.get(1).file.name).        thenReturn("MediaFile1")
        Mockito.`when`(arrayMediaFile.get(1).file.absolutePath).thenReturn("MediaFile1Path")

        Mockito.`when`(arrayMediaFile.get(2).file.name).        thenReturn("MediaFile2")
        Mockito.`when`(arrayMediaFile.get(2).file.absolutePath).thenReturn("MediaFile2Path")

        Mockito.`when`(arrayMediaFile.get(3).file.name).        thenReturn("MediaFile3")
        Mockito.`when`(arrayMediaFile.get(3).file.absolutePath).thenReturn("MediaFile3Path")
    }
}