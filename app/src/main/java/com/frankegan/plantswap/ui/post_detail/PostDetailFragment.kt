package com.frankegan.plantswap.ui.post_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frankegan.plantswap.R
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.databinding.PostDetailFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val viewModel by viewModels<PostDetailViewModel>()

    private val binding by viewBinding(PostDetailFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.post_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        val idFromArgs = PlantPostId(requireArguments().getString("plantPostId", ""))
        viewModel.plantPost(idFromArgs).observe(viewLifecycleOwner) { plantPost ->
            binding.text.text = plantPost.toString()
            binding.toolbar.title = plantPost.getOrNull()?.title ?: "Error"
        }
    }

    companion object {
        fun newInstance(plantPostId: PlantPostId): PostDetailFragment {
            return PostDetailFragment().apply {
                arguments = bundleOf(
                    "plantPostId" to plantPostId.id
                )
            }
        }
    }
}