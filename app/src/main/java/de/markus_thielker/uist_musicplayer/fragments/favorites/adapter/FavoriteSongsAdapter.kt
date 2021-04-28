package de.markus_thielker.uist_musicplayer.fragments.favorites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.databinding.ItemCardSongSmallBinding

class FavoriteSongsAdapter(
    private val context: Context,
    private val onItemClickListener: (item: Song) -> Unit
) :
    ListAdapter<Song, FavoriteSongsAdapter.ViewHolder>(SongDiffCallback()) {

    class ViewHolder(
        private val binding: ItemCardSongSmallBinding,
        private val onItemClickListener: (item: Song) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Song) {

            binding.apply {

            lblTitle.text = item.title
                lblArtist.text = item.artist

                // if cover-src is available -> load image
                if (item.srcCover.isNotEmpty()) {
                    Glide.with(context)
                        .load(item.srcCover)
                        .placeholder(R.drawable.icon_music)
                        .into(imgView)
                }

                layoutCardContent.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardSongSmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClickListener
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val song = getItem(position)
        viewHolder.bind(context, song)
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
