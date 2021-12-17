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
interface TripDataDao {
    @Query("SELECT * from Trip ORDER by id ASC")
    suspend fun getItems(): List<TripData>

    @Query("SELECT * from Trip WHERE id = :id")
    fun getItem(id: Int): TripData

    // Specify the conflict strategy as REPLACE,
    // when the trying to add an existing Item
    // into the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(singleTripData: TripData): Long


    @Update
    suspend fun update(tripData: TripData)

    @Delete
    suspend fun delete(tripData: TripData)
}