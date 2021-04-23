package de.markus_thielker.uist_musicplayer.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.databinding.FragmentPlayerBinding

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
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}