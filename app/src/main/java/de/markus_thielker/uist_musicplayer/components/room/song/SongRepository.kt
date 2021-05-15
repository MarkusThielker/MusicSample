package de.markus_thielker.uist_musicplayer.components.room.song

import android.app.Application
import de.markus_thielker.uist_musicplayer.components.room.AppDatabase

class SongRepository(application: Application) {

    private val songDao = AppDatabase.getInstance(application).songDao()

    fun getAll() = songDao.getAll()

    fun getFavorites() = songDao.getFavorites()

    fun findById(songId: Int) = songDao.findById(songId)

    fun findByString(string: String) = songDao.findByString(string)

    fun updateSong(song: Song) = songDao.updateSong(song)
}