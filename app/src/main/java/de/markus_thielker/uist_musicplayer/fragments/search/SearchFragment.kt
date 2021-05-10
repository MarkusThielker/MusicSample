package de.markus_thielker.uist_musicplayer.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.ASSERT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.components.room.song.Song
import de.markus_thielker.uist_musicplayer.databinding.FragmentSearchBinding
import de.markus_thielker.uist_musicplayer.fragments.favorites.adapter.FavoriteSongsAdapter
import de.markus_thielker.uist_musicplayer.fragments.player.PlayerViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    // player view model access
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // view binding variables
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    /** executed on fragment startup */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.txtSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playerViewModel.updateSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.txtSearch.setText(playerViewModel.searchString.value)

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
        val searchSongsAdapter = FavoriteSongsAdapter(requireContext(), onItemClickListener)

        // setup recycler view -> bind adapter
        binding.recyclerView.apply {
            adapter = searchSongsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // set observer for song list
        playerViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            // submit new dataset to adapter
            searchSongsAdapter.submitList(results)
            Log.println(ASSERT, "SearchFragment", "List updated")
        }
    }

    /** executed on fragment destroy */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}