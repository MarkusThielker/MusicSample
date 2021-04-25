package de.markus_thielker.uist_musicplayer.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.databinding.ItemCardSongLargeBinding


class SongsAdapter : ListAdapter<Song, SongsAdapter.ViewHolder>(SongDiffCallback()) {

    class ViewHolder(private val binding: ItemCardSongLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Song) {
            binding.apply {
                lblTitle.text = item.title
                lblArtist.text = item.artist
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardSongLargeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val song = getItem(position)
        viewHolder.bind(song)
    }
}

private class SongDiffCallback : DiffUtil.ItemCallback<Song>() {

    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.sid == newItem.sid
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}
