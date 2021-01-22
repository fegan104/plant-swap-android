package com.frankegan.plantswap.ui.nearby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.frankegan.plantswap.R
import com.frankegan.plantswap.databinding.NearbyFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import com.frankegan.plantswap.ui.create_post.CreatePostFragment
import com.frankegan.plantswap.ui.post_detail.PostDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyFragment : Fragment() {

    private val viewModel by viewModels<NearbyViewModel>()

    private val binding by viewBinding(NearbyFragmentBinding::bind)

    private val adapter = NearbyAdapter { post ->
        parentFragmentManager.commit {
            findNavController().navigate(
                NearbyFragmentDirections.actionNearbyToPostDetail(post.id.id)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nearby_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.posts.adapter = adapter

        binding.addPostIcon.setOnClickListener {
            parentFragmentManager.commit {
                findNavController().navigate(NearbyFragmentDirections.actionNearbyToCreatePost())
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