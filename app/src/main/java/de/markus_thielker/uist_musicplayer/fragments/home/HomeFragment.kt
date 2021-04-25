package de.markus_thielker.uist_musicplayer.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import de.markus_thielker.uist_musicplayer.R
import de.markus_thielker.uist_musicplayer.databinding.FragmentHomeBinding
import de.markus_thielker.uist_musicplayer.fragments.home.adapter.SongsAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    // view model variable
    private lateinit var homeViewModel: HomeViewModel

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

        // create home view model
        homeViewModel = ViewModelProvider
            .AndroidViewModelFactory(requireActivity().application)
            .create(HomeViewModel::class.java)

        setupRecyclerView()

        return binding.root
    }

    /**
     * Called to setup recycler view with songs adapter
     * and setup observer on song list.
     *
     * */
    private fun setupRecyclerView() {

        // create adapter object
        val songsAdapter = SongsAdapter()

        // setup recycler view -> bind adapter
        binding.recyclerView.apply {
            adapter = songsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
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