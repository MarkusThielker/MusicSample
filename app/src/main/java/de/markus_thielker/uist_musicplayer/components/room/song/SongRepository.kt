package de.markus_thielker.uist_musicplayer.components.room.song

import android.app.Application
import de.markus_thielker.uist_musicplayer.components.room.AppDatabase

class SongRepository(application: Application) {

    private val songDao = AppDatabase.getInstance(application).songDao()

    fun getAll() = songDao.getAll()
}