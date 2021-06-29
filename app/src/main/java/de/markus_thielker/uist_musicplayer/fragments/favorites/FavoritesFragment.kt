package de.markus_thielker.uist_musicplayer.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.databinding.FragmentFavoritesBinding
import de.markus_thielker.uist_musicplayer.fragments.favorites.adapter.FavoriteSongsAdapter
import de.markus_thielker.uist_musicplayer.fragments.player.PlayerViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    // player view model access
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // view binding variables
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    /** executed on fragment startup */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // setup recycler view
        setupRecyclerView()

        return binding.root
    }

    /**
     * Called to setup recycler view with songs adapter
     * and setup observer on song list.
     *
     * */
    private fun setupRecyclerView() {

        // create click listener for adapter items
        val onItemClickListener: (Song) -> Unit = { item ->
            playerViewModel.updateCurrentSong(item)
            findNavController().navigate(R.id.action_global_navigationFragmentPlayer)
        }

        // create adapter object
        val favoriteSongsAdapter = FavoriteSongsAdapter(requireContext(), onItemClickListener)

        // setup recycler view -> bind adapter
        binding.recyclerView.apply {
            adapter = favoriteSongsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // set observer for song list
        playerViewModel.favorites.observe(viewLifecycleOwner) { songs ->
            // submit new dataset to adapter
            favoriteSongsAdapter.submitList(songs)
        }
    }

    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()

        // remove observer on view destroy
        playerViewModel.favorites.removeObservers(viewLifecycleOwner)

        _binding = null
    }
}