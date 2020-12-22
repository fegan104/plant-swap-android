package com.frankegan.plantswap

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.data.remote.AuthenticationInterceptor
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.databinding.MainActivityBinding
import com.frankegan.plantswap.extensions.viewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject


private const val RC_SIGN_IN = 1000

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var plantRepository: PlantRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var authUI: AuthUI

    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        firebaseAuth.currentUser?.let { user ->
            binding.accountIcon.load(user.photoUrl) {
                transformations(CircleCropTransformation())
            }
        }

        binding.accountIcon.setOnClickListener {
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                finish()
                return@setOnClickListener
            }
            startActivityForResult(
                authUI
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            GoogleBuilder().build(),
                            EmailBuilder().build()
                        )
                    )
                    .build(),
                RC_SIGN_IN
            )
        }

        binding.addPostIcon.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val result = plantRepository.createPosts(
                    postTitle = "Post with Location",
                    postDescription = "A post containing a location",
                    currentLocation = userRepo.getCurrentLocation().getOrThrow()
                )
                Log.d("MainActivity", result.toString())
            }
        }

        binding.loadButton.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val result = plantRepository.getUser()
                binding.contentText.text = result.toString()
            }
        }

        lifecycleScope.launchWhenCreated {
            val currentLocation = userRepo.getCurrentLocation().getOrNull() ?: return@launchWhenCreated
            plantRepository.getNearbyPlants(currentLocation).fold(
                onSuccess = { Log.d("MainActivity", it.toString()) },
                onFailure = { Log.e("MainActivity", it.localizedMessage, it) }
            )

            plantRepository.getConversations("bill_user_id").addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("MainActivity", error.localizedMessage, error)
                } else {
                    Log.d("MainActivity", value?.documents
                        ?.map { it.reference.parent.parent?.path }
                        .toString()
                    )
                }
            }

            plantRepository.getPlantPosts("MTqInG7sWrOuxLYXo8hAumc2XQl2").addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("MainActivity", error.localizedMessage, error)
                } else {
                    Log.d("MainActivity", value?.documents?.map { it.data }.toString())
                }
            }
        }
    }
}