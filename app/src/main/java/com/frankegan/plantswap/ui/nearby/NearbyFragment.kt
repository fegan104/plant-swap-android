package com.frankegan.plantswap.ui.nearby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.frankegan.plantswap.R
import com.frankegan.plantswap.databinding.NearbyFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import com.frankegan.plantswap.ui.create_post.CreatePostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyFragment : Fragment() {

    private val viewModel by viewModels<NearbyViewModel>()

    private val adapter = NearbyAdapter()

    private val binding by viewBinding(NearbyFragmentBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nearby_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.posts.adapter = adapter

        binding.addPostIcon.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.main_container, CreatePostFragment.newInstance())
            }
        }

        viewModel.nearbyPlantPosts().observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { plants ->
                    adapter.plantPosts = plants
                },
                onFailure = ::renderError
            )
        }
    }

    private fun renderError(throwable: Throwable) {
        Log.e("NearbyFragment", throwable.localizedMessage, throwable)
    }

    companion object {

        fun newInstance() = NearbyFragment()
    }
}