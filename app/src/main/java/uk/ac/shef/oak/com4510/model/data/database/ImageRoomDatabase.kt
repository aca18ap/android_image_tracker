package uk.ac.shef.oak.com4510.model.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [ImageData::class, ImageDataFTS::class], version = 4, exportSchema = false)
abstract class ImageRoomDatabase: RoomDatabase() {

    abstract fun imageDataDao(): ImageDataDao

    companion object{
        @Volatile
        private var INSTANCE: ImageRoomDatabase? = null
        fun getDatabase(context: Context): ImageRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageRoomDatabase::class.java,
                    "travelApp_database"
                )
                    // Rebuilds the FTS index. This is only necessary while debugging
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("INSERT INTO image_fts(image_fts) VALUES('rebuild')")
                        }
                    })
                    // Wipes and rebuilds instead of migrating if no Migration object specified.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}