package de.markus_thielker.uist_musicplayer.components.room.song

import androidx.lifecycle.LiveData
import androidx.room.*
import de.markus_thielker.uist_musicplayer.utilities.SONGS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM $SONGS_TABLE_NAME")
    fun getAll(): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE favorite == 1")
    fun getFavorites(): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE sid == :songId")
    fun findById(songId: Int): LiveData<List<Song>>

    @Query("SELECT * FROM $SONGS_TABLE_NAME WHERE title LIKE '%' || :string || '%' OR artist LIKE '%' || :string || '%' OR feature LIKE '%' || :string || '%'")
    fun findByString(string: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg songs: Song)

    @Update
    fun updateSong(song: Song)
}