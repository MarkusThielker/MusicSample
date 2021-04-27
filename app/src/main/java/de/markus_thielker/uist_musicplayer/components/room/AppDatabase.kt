package de.markus_thielker.uist_musicplayer.components.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.components.room.song.SongDao
import de.markus_thielker.uist_musicplayer.utilities.DATABASE_NAME


/**
 * This class represents the foundation of the room database by managing
 * the instance count and populating the database on creation.
 *
 * */
@Database(entities = [Song::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    // singleton implementation
    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        // check for existing instance
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // build database with custom callback
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // enqueue worker to populate db with sample data
                            val worker = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(worker)
                        }
                    }
                )
                .build()
        }
    }
}