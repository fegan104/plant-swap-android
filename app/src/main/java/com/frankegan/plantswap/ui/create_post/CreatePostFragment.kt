package com.frankegan.plantswap.ui.create_post

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frankegan.plantswap.R

class CreatePostFragment : Fragment() {

    companion object {

        fun newInstance() = CreatePostFragment()
    }

    private lateinit var viewModel: CreatePostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_post_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreatePostViewModel::class.java)
        // TODO: Use the ViewModel
    }

}