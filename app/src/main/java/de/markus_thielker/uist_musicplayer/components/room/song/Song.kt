package de.markus_thielker.uist_musicplayer.components.room.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.markus_thielker.uist_musicplayer.utilities.SONGS_TABLE_NAME

@Entity(tableName = SONGS_TABLE_NAME)
data class Song(
    @PrimaryKey(autoGenerate = true) val sid: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "feature") val feature: String?,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "favorite") var favorite: Boolean,
    @ColumnInfo(name = "srcAudio") val srcAudio: String,
    @ColumnInfo(name = "srcCover") val srcCover: String,
)