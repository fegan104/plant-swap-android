package com.frankegan.plantswap.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.firebase.ui.auth.AuthUI
import com.frankegan.plantswap.R
import com.frankegan.plantswap.databinding.AccountFragmentBinding
import com.frankegan.plantswap.extensions.viewBinding
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val RC_SIGN_IN = 1000

@AndroidEntryPoint
class AccountFragment : Fragment() {

    @Inject
    lateinit var authUi: AuthUI

    private val viewModel by viewModels<AccountViewModel>()

    private val binding by viewBinding(AccountFragmentBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentUser().observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = ::renderUser,
                onFailure = ::renderError
            )
        }
    }

    private fun renderUser(firebaseUser: FirebaseUser) {
        with(binding) {
            accountGroup.isVisible = true
            signInButton.isVisible = false
            signOut.setOnClickListener {
                viewModel.signOut()
            }
            avatar.load(firebaseUser.photoUrl) {
                placeholder(R.drawable.ic_baseline_account_circle_24)
                error(R.drawable.ic_baseline_account_circle_24)
                transformations(CircleCropTransformation())
            }
            name.text = firebaseUser.displayName
        }
    }

    private fun renderError(throwable: Throwable) {
        binding.accountGroup.isVisible = false
        binding.signInButton.isVisible = true
        binding.signInButton.setOnClickListener {
            startActivityForResult(
                authUi
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.EmailBuilder().build()
                        )
                    )
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            RC_SIGN_IN -> {
                viewModel.userSignedIn()
            }
        }
    }

    companion object {

        fun newInstance() = AccountFragment()
    }
}