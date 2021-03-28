package com.example.instaweather.ui.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.instaweather.R
import com.example.instaweather.databinding.FragmentSearchBinding


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        binding.searchImageLayout.visibility = View.VISIBLE
        binding.cardView.visibility = View.INVISIBLE
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
    }

}