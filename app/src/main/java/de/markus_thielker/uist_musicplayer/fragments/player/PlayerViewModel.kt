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
import java.util.*
import kotlin.concurrent.timerTask

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val songRepository = SongRepository(application)

    val songs: LiveData<List<Song>> = songRepository.getAll()


    // variables and functions for current song
    private val _currentSong: MutableLiveData<Song?> =
        MutableLiveData<Song?>().apply { value = null }

    val currentSong: LiveData<Song?> = _currentSong

    /**
     * Updates the current song in the view model.
     * Also updates all variables used for further processing
     * and schedules progress timer.
     *
     * */
    fun updateCurrentSong(item: Song) {
        _currentSong.value = item
        secondsLeft = item.duration
        _currentlyPlaying.value = true

        scheduleSongProgress()
    }


    // variables and functions for song progress
    private val _songProgress: MutableLiveData<Double> =
        MutableLiveData<Double>().apply { value = 0.00 }

    val songProgress: LiveData<Double> = _songProgress

    // storing the seconds left for this song to survive timer cancellation
    private var secondsLeft: Int = 0

    // timer, the fake playback is scheduled on
    private var timer = Timer()

    /**
     * Schedules a timer task, calculating the current progress of th song playback
     * in percent every second.
     *
     * */
    private fun scheduleSongProgress() {

        // reset timer
        timer.cancel()
        timer = Timer()

        // schedule countdown task for every second
        timer.scheduleAtFixedRate(

            // countdown task
            timerTask {

                // countdown while time left
                if (secondsLeft > 0) {

                    // count down and update progress
                    secondsLeft -= 1
                    _songProgress
                        .postValue((((currentSong.value?.duration?.toDouble()!! / secondsLeft) - 1) * 100) / 2)

                } else {
                    // cancel after time ran out
                    timer.cancel()
                }

            }, 1000, 1000
        )
    }


    // variables and functions for currently playing song
    private val _currentlyPlaying: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    val currentlyPlaying: LiveData<Boolean> = _currentlyPlaying

    /**
     * Updates boolean variable in view model and stops or
     * schedules progress timer according to the new playback state.
     *
     * */
    fun updateCurrentlyPlaying() {

        // cancel timer if music stopped, else schedule timer
        if (_currentlyPlaying.value == true) {
            timer.cancel()
        } else {
            scheduleSongProgress()
        }

        // negate currentlyPlaying
        _currentlyPlaying.value = !_currentlyPlaying.value!!
    }


    /**
     * Updates the "marked as favorite" value for the song in the database.
     *
     * */
    fun updateSongFavorite(item: Song) {

        // update current song to trigger observer -> ui icon changed
        _currentSong.value = _currentSong.value?.let { it.apply { favorite = !favorite } }

        // update song in database
        CoroutineScope(Dispatchers.IO).launch {
            songRepository.updateSong(item)
        }
    }

    /** Called at the end of the lifecycle */
    override fun onCleared() {
        super.onCleared()

        timer.cancel()
    }
}