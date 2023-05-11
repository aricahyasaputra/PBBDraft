package com.example.pbbdraft.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PBB::class],
    version = 1,
    exportSchema = false

)
abstract class PBBDB : RoomDatabase(){

    abstract fun PBBDao() : PBBDao

    companion object {

        @Volatile private var instance : PBBDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PBBDB::class.java,
            "datapajak"
        ).createFromAsset("database/sample.db").build()

    }
}