package de.markus_thielker.uist_musicplayer.fragments.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.components.room.song.SongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val songRepository = SongRepository(application)

    val songs: LiveData<List<Song>> = songRepository.getAll()

    private val _currentSong: MutableLiveData<Song?> =
        MutableLiveData<Song?>().apply { value = null }

    val currentSong: LiveData<Song?> = _currentSong

    fun updateCurrentSong(item: Song) {
        _currentSong.value = item
    }

    fun updateSongFavorite(item: Song) {

        // update current song to trigger observer -> ui icon changed
        _currentSong.value = _currentSong.value?.let { it.apply { favorite = !favorite } }

        // update song in database
        CoroutineScope(Dispatchers.IO).launch {
            songRepository.updateSong(item)
        }
    }
}