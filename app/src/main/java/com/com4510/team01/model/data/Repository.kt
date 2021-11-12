package com.com4510.team01.model.data

import android.app.Application
import com.com4510.team01.model.data.database.ImageDataDao
import com.com4510.team01.model.data.database.ImageRoomDatabase

class Repository(application: Application) {
    private var imageDataDao: ImageDataDao? = null

    init {
        val db: ImageRoomDatabase? = ImageRoomDatabase.getDatabase(application)
        if (db != null) { imageDataDao = db.imageDataDao() }
    }

    /*
    suspend fun insertInBackground(vararg params: NumberData) = withContext(Dispatchers.IO)
    {
        for(param in params){
            val insertedId: Int? = mDBDao?.insert(param)?.toInt()
            // you may want to check if insertedId is null to confirm successful insertion
            Log.i("MyRepository", "number generated: " + param.number.toString()
                    + ", inserted with id: " + insertedId.toString() + "")
        }
    }
    */

    /**
     * called by the UI to request the generation of a new random number
     */
    /*
    suspend fun generateNewNumber() {
        val r = Random()
        val i1 = r.nextInt(10000 - 1) + 1
        InsertAsyncTask(mDBDao).insertInBackground(NumberData(number = i1))
    }
    */

}