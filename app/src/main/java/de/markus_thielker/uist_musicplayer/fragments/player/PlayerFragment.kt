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

        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val likeSongButton = binding.likeSongButton
        likeSongButton.setOnClickListener {
            playerViewModel.currentSong.value?.let { playerViewModel.updateSongFavorite(it) }
        }

        val previousSong = binding.previousSong
        previousSong.setOnClickListener {
            //TODO play previous song
        }

        val nextSong = binding.nextSong
        nextSong.setOnClickListener {
            //TODO play next song
        }

        val stopMusic = binding.stopMusic
        stopMusic.setOnClickListener {
            //TODO stop the music
        }

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