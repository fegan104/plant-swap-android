package com.frankegan.plantswap.ui.messages

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.frankegan.plantswap.R

class MessagesFragment : Fragment() {

    private val viewModel by viewModels<MessagesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_fragment, container, false)
    }

    companion object {

        fun newInstance() = MessagesFragment()
    }

}