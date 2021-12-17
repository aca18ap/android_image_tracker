package uk.ac.shef.oak.com4510.model.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ImageDataDao {
    @Query("SELECT * from image ORDER by id ASC")
    suspend fun getItems(): List<ImageData>


    // Possible future bug: This JOIN is not happening on primary keys. I'm no database expert, but I
    // assume this could lead to issues where you have multiple images having the same title?
    /**
     * Returns a list of ImageData whose title OR description contain a keyword in a given query
     */
    @Query("""
    SELECT *
    FROM image
    JOIN image_fts ON image.title = image_fts.title
    WHERE image_fts MATCH :query
  """)
    suspend fun search(query: String): List<ImageData>

    @Query("SELECT * from image WHERE id = :id")
    fun getItem(id: Int): ImageData

    @Query("SELECT * from image WHERE entry_id = :entryId")
    fun getEntryImages(entryId: Int): List<ImageData>

    // Specify the conflict strategy as REPLACE,
    // when the trying to add an existing Item
    // into the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(singleImageData: ImageData): Long

    @Update
    suspend fun update(imageData: ImageData)

    @Delete
    suspend fun delete(imageData: ImageData)
}