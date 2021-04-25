package de.markus_thielker.uist_musicplayer.components.room.song

import android.app.Application
import de.markus_thielker.uist_musicplayer.components.room.AppDatabase

class SongRepository(application: Application) {

    private val songDao = AppDatabase.getInstance(application).songDao()

    fun getAll() = songDao.getAll()

    fun findById(songId: Int) = songDao.findById(songId)

    fun findByTitle(title: String) = songDao.findByTitle(title)

    fun findByArtist(artist: String) = songDao.findByArtist(artist)

    fun updateSong(song: Song) = songDao.updateSong(song)
}