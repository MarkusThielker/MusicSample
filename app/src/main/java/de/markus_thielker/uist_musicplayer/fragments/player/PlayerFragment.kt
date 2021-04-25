package de.markus_thielker.uist_musicplayer.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.databinding.FragmentPlayerBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController


class PlayerFragment : Fragment(R.layout.fragment_player) {

    // view binding variables
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    /** executed on fragment startup */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
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
            //TODO add song to liked songs
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
    }

    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as AppCompatActivity?)!!.supportActionBar!!.show()
        _binding = null
    }
}