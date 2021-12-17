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
interface EntryDataDao {
    @Query("SELECT * from Entry ORDER by id ASC")
    suspend fun getItems(): List<EntryData>

    //TO DO: Make this function work. I
    @Query("SELECT * from Entry WHERE trip_id = :tripId")
    suspend fun getEntriesForTrip(tripId : Int) : List<EntryData>

    @Query("SELECT * from Entry WHERE id = :id")
    fun getItem(id: Int): EntryData

    @Query("SELECT * \n" +
            "FROM Entry \n" +
            "WHERE id=(\n" +
            "    SELECT max(id) FROM Entry\n" +
            "    )")
    suspend fun getLastEntryById() : EntryData

    // Specify the conflict strategy as REPLACE,
    // when the trying to add an existing Item
    // into the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entryData: EntryData): Long

    @Update
    suspend fun update(entryData: EntryData)

    @Delete
    suspend fun delete(entryData: EntryData)
}