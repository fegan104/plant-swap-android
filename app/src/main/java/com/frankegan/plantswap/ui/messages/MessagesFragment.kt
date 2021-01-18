package com.frankegan.plantswap.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.frankegan.plantswap.R
import com.frankegan.plantswap.databinding.CreatePostFragmentBinding
import com.frankegan.plantswap.databinding.MessagesFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    @Inject
    lateinit var adapter: ConversationsAdapter

    private val binding by viewBinding(MessagesFragmentBinding::bind)

    private val viewModel by viewModels<MessagesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.messages_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.conversations.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.conversations().collect {
                adapter.submitData(it)
            }
        }

    }

    companion object {

        fun newInstance() = MessagesFragment()
    }

}