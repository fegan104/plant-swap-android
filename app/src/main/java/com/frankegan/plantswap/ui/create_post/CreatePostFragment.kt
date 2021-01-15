package com.frankegan.plantswap.ui.create_post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frankegan.plantswap.R
import com.frankegan.plantswap.databinding.CreatePostFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private val viewModel by viewModels<CreatePostViewModel>()

    private val binding by viewBinding(CreatePostFragmentBinding::bind)

    @Inject
    lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        svedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_post_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gallery.adapter = adapter

        viewModel.createPostStatus().observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { parentFragmentManager.popBackStack() },
                onFailure = { Log.e("CreatePostFragment", it.message, it) }
            )
        }

        binding.save.setOnClickListener {
            viewModel.createPosts(
                postTitle = binding.title.text.toString(),
                postDescription = binding.details.text.toString()
            )
        }

        viewModel.attachedPhotos().observe(viewLifecycleOwner) {
            adapter.photos = it
        }

        binding.addPhotos.setOnClickListener {
            val openGalleryIntent = Intent().apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(openGalleryIntent, 200)
        }
    }

    @ExperimentalStdlibApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val clipData = data?.clipData ?: return
        viewModel.attachPhotos(
            buildList {
                for (i in 0 until clipData.itemCount) {
                    add(clipData.getItemAt(i).uri)
                }
            }
        )
    }

    companion object {

        fun newInstance() = CreatePostFragment()
    }
}
