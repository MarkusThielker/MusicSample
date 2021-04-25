package de.markus_thielker.uist_musicplayer.components.room.song

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.markus_thielker.uist_musicplayer.utilities.SONGS_TABLE_NAME

@Dao
interface SongDao {

    @Query("SELECT * FROM $SONGS_TABLE_NAME")
    fun getAll(): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE sid == :songId")
    fun findById(songId: Int): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE title LIKE :title")
    fun findByName(title: String): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE artist LIKE :artist")
    fun findByArtist(artist: String): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg songs: Song)
}