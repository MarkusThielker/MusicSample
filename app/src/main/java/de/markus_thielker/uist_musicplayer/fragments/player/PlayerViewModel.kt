package de.markus_thielker.uist_musicplayer.fragments.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.components.room.song.SongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    // song repository
    private val songRepository = SongRepository(application)

    // list of all songs
    val songs: LiveData<List<Song>> = songRepository.getAll()

    // list of all songs
    val favorites: LiveData<List<Song>> = songRepository.getFavorites()

    // selected song
    private val _currentSong: MutableLiveData<Song?> = MutableLiveData<Song?>(null)
    val currentSong: LiveData<Song?> = _currentSong

    // state if selected song is playing
    private val _currentlyPlaying: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val currentlyPlaying: LiveData<Boolean> = _currentlyPlaying

    // song progress of the selected song
    private val _songProgress: MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    val songProgress: LiveData<Double> = _songProgress

    // storing the seconds left, to survive timer cancellation
    private var secondsLeft: Int = 0

    // timer, the fake playback is scheduled on
    private var timer = Timer()

    // state flow for search string
    val searchString = MutableStateFlow("")

    // search results live data, based on search string
    @ExperimentalCoroutinesApi
    val searchResults = searchString.flatMapLatest { songRepository.findByString(it) }.asLiveData()

    /** Called at the end of the lifecycle */
    override fun onCleared() {
        super.onCleared()

        timer.cancel()
    }

    /**
     * Updates the current song in the view model.
     * Also updates all variables used for further processing
     * and schedules progress timer.
     *
     * */
    fun updateCurrentSong(item: Song) {

        if (item == currentSong.value) return

        _currentSong.value = item
        secondsLeft = item.duration
        _currentlyPlaying.value = true

        scheduleSongProgress()
    }

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
        _currentlyPlaying.postValue(!_currentlyPlaying.value!!)
    }

    /** Updates the "marked as favorite" value for the song in the database. */
    fun updateSongFavorite(item: Song) {

        // update current song to trigger observer -> ui icon changed
        _currentSong.value = _currentSong.value?.let { it.apply { favorite = !favorite } }

        // update song in database
        CoroutineScope(Dispatchers.IO).launch {
            songRepository.updateSong(item)
        }
    }

    /**
     * Schedules a timer task, calculating the current progress of th song playback
     * in percent every second.
     *
     * */
    private fun scheduleSongProgress() {

        // reset timer
        timer.cancel()
        timer = Timer()

        val duration = currentSong.value!!.duration.toDouble()

        // schedule countdown task for every second
        timer.scheduleAtFixedRate(

            // countdown task
            timerTask {

                // countdown while time left
                if (secondsLeft > 0) {

                    // count down and update progress
                    secondsLeft -= 1
                    _songProgress.postValue((1 - (secondsLeft / duration)) * 100)

                } else {
                    // cancel after time ran out
                    timer.cancel()
                    updateCurrentlyPlaying()
                }

            }, 1000, 1000
        )
    }

    /** Update the value of the search string */
    fun updateSearch(newSearchString: String) {
        searchString.value = newSearchString
    }

    /** Called on seek bar progress changes */
    fun onProgressChanged(progress: Int) {

        val duration = currentSong.value!!.duration.toDouble()
        val passedSecs = duration * (progress / 100.0)
        secondsLeft = (duration - passedSecs).toInt()

        _currentlyPlaying.value = true
        scheduleSongProgress()
    }
}