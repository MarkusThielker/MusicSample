package de.markus_thielker.uist_musicplayer.fragments.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.components.room.song.SongRepository

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val songRepository = SongRepository(application)

    val songs: LiveData<List<Song>> = songRepository.getAll()

    private val _currentSong: MutableLiveData<Song?> = MutableLiveData<Song?>().apply{value=null}

    val currentSong: LiveData<Song?> = _currentSong

    fun updateCurrentSong(item: Song){
        _currentSong.value = item
    }
}