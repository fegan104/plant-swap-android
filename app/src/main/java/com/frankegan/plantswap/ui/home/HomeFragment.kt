package com.frankegan.plantswap.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.frankegan.plantswap.databinding.HomeFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private val adapter = HomeAdapter()

    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.posts.adapter = adapter
    }

    companion object {

        fun newInstance() = HomeFragment()
    }
}