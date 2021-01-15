package com.frankegan.plantswap

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.databinding.MainActivityBinding
import com.frankegan.plantswap.extensions.viewBinding
import com.frankegan.plantswap.ui.account.AccountFragment
import com.frankegan.plantswap.ui.nearby.NearbyFragment
import com.frankegan.plantswap.ui.messages.MessagesFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.commit {
            replace(R.id.main_container, NearbyFragment.newInstance())
        }

        binding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            val navTarget = when(item.itemId) {
                R.id.nearby -> NearbyFragment.newInstance()
                R.id.messages -> MessagesFragment.newInstance()
                R.id.account -> AccountFragment.newInstance()
                else -> throw IllegalArgumentException()
            }
            supportFragmentManager.commit {
                replace(R.id.main_container, navTarget)
            }
            true
        }
    }
}