package de.markus_thielker.uist_musicplayer.components.room

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.utilities.SAMPLE_DATA_FILENAME

class PopulateDatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            applicationContext.assets.open(SAMPLE_DATA_FILENAME).use { inputStream ->

                inputStream.reader().use { reader ->

                    val songType = object : TypeToken<List<Song>>() {}.type
                    val plantList: List<Song> = Gson().fromJson(reader, songType)
                    val database = AppDatabase.getInstance(applicationContext)
                    database.songDao().insertAll(*plantList.toTypedArray())

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e("PopulateDatabaseWorker", "Error populating database", ex)
            Result.failure()
        }
    }
}