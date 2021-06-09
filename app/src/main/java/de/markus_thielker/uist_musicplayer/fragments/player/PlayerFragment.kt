package de.markus_thielker.uist_musicplayer.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment(R.layout.fragment_player) {

    // view model variable
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // view binding variables
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    /** executed on fragment startup */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        injectDataIntoPlayer()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // click listener to navigate backwards
        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // click listener to favorite button
        val likeSongButton = binding.likeSongButton
        likeSongButton.setOnClickListener {
            playerViewModel.currentSong.value?.let { playerViewModel.updateSongFavorite(it) }
        }

        // click listener to play previous song
        val previousSong = binding.previousSong
        previousSong.setOnClickListener {
            //TODO play previous song
        }

        // click listener to play next song
        val nextSong = binding.nextSong
        nextSong.setOnClickListener {
            //TODO play next song
        }

        // click listener to play/pause the music
        val stopMusic = binding.stopMusic
        stopMusic.setOnClickListener {
            playerViewModel.updateCurrentlyPlaying()
        }

        // observer song progress and update slider
        playerViewModel.songProgress.observe(viewLifecycleOwner) { progress ->
            binding.musicPlayTime.progress = progress.toInt()
        }

        // observe is song is playing and change play/pause icon accordingly
        playerViewModel.currentlyPlaying.observe(viewLifecycleOwner) { playing ->
            if (playing) binding.stopMusic.setImageResource(R.drawable.icon_pause)
            else binding.stopMusic.setImageResource(R.drawable.icon_play)
        }

        // observe current song and check on change, if song is marked as favorite
        playerViewModel.currentSong.observe(viewLifecycleOwner) { currentSong ->

            currentSong?.let {
                if (currentSong.favorite) {
                    binding.likeSongButton.setImageResource(R.drawable.icon_favorites_fill)
                } else {
                    binding.likeSongButton.setImageResource(R.drawable.icon_nav_favorites)
                }
            }
        }
    }

    /**
     * Called to display data in the player view with songs adapter
     * and setup observer on song list.
     *
     * */
    private fun injectDataIntoPlayer() {

        // set observer for song list
        playerViewModel.currentSong.observe(viewLifecycleOwner) { currentSong ->

            currentSong?.let {

                // bind strings to text views
                binding.songName.text = currentSong.title
                binding.artistName.text = currentSong.artist

                // if cover-src is available -> load image
                if (currentSong.srcCover.isNotEmpty()) {
                    Glide.with(requireContext())
                        .load(currentSong.srcCover)
                        .placeholder(R.drawable.icon_music)
                        .into(binding.songImage)
                }
            }
        }
    }


    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()

        // remove observer on view destroy
        playerViewModel.songs.removeObservers(viewLifecycleOwner)

        _binding = null
    }
}