package com.frankegan.plantswap.ui.direct_message

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frankegan.plantswap.R

class DirectMessageFragment : Fragment() {

    companion object {

        fun newInstance() = DirectMessageFragment()
    }

    private lateinit var viewModel: DirectMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.direct_message_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DirectMessageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}