package com.tuan.englishforkid.roomdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Favor::class, Prac::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

//        fun insertFavorAsync(context: Context, favor: Favor) {
//            val db = getDatabase(context)
//            CoroutineScope(Dispatchers.IO).launch {
//                db.dao().insertfavor(favor)
//            }
//        }
//        fun deleteFavorByIdAsync(context: Context, id: Int?) {
//            val db = getDatabase(context)
//            CoroutineScope(Dispatchers.IO).launch {
//                if (id != null) {
//                    db.dao().deleteById(id)
//                }
//            }
//        }
//
//        fun insertPracAsync(context: Context, prac: Prac) {
//            val db = getDatabase(context)
//            CoroutineScope(Dispatchers.IO).launch {
//                db.dao().insert(prac)
//            }
//        }

    }
