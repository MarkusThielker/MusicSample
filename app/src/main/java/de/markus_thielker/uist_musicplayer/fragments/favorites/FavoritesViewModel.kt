package de.markus_thielker.uist_musicplayer.fragments.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.components.room.song.SongRepository

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val songRepository = SongRepository(application)

    val favorites: LiveData<List<Song>> = songRepository.getFavorites()
}