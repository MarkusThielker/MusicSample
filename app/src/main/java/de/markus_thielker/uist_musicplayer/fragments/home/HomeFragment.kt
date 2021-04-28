package de.markus_thielker.uist_musicplayer.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.databinding.FragmentHomeBinding
import de.markus_thielker.uist_musicplayer.fragments.home.adapter.SongsAdapter
import de.markus_thielker.uist_musicplayer.fragments.player.PlayerViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    // home view model access
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(
            requireActivity().application
        )
    }

    // player view model access
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // view binding variables
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /** executed on fragment startup */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    /**
     * Called to setup recycler view with songs adapter
     * and setup observer on song list.
     *
     * */
    private fun setupRecyclerView() {

        val onItemClickListener: (Song) -> Unit = { item ->
            playerViewModel.updateCurrentSong(item)
            findNavController().navigate(R.id.action_global_navigationFragmentPlayer)
        }

        // create adapter object
        val songsAdapter = SongsAdapter(requireContext(), onItemClickListener)

        // setup recycler view -> bind adapter
        binding.recyclerView.apply {
            adapter = songsAdapter
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        }

        // set observer for song list
        homeViewModel.songs.observe(viewLifecycleOwner) { songs ->
            // submit new dataset to adapter
            songsAdapter.submitList(songs)
        }
    }

    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()

        // remove observer on view destroy
        homeViewModel.songs.removeObservers(viewLifecycleOwner)

        _binding = null
    }
}