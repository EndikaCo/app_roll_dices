package com.endcodev.roll_dices.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.endcodev.roll_dices.R
import com.endcodev.roll_dices.databinding.ActivityMainBinding
import com.endcodev.roll_dices.domain.models.ErrorModel
import com.endcodev.roll_dices.domain.utils.App
import com.endcodev.roll_dices.presentation.dialog.ErrorDialogFragment
import com.endcodev.roll_dices.presentation.utils.StoreUtils.getVersion
import com.endcodev.roll_dices.presentation.utils.StoreUtils.openPlayStore

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isConnected()

        initObservers()

        initNavController()

        appReady()
    }

    /** Initializes observers for LiveData objects in the ViewModel.*/
    private fun initObservers() {
        mainViewModel.version.observe(this) {
            if (it != null)
                versionControl(it)
            else
                Log.e(App.tag, "App version is null")
        }
    }

    /** Sets up an OnPreDrawListener to check whether the initial data is ready before drawing the UI.*/
    private fun appReady() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (mainViewModel.isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else
                        false
                }
            }
        )
    }

    /**
     * Checks whether the current app version matches the required version and shows an error dialog if it doesn't.
     * @param needVersion The required version as a String.
     */
    private fun versionControl(needVersion: String) {

        val error =
            ErrorModel(
                getString(R.string.require_update),
                getString(
                    R.string.require_version,
                    getVersion(this).toString(),
                    needVersion.toInt()
                ),
                getString(R.string.update),
                getString(R.string.cancel)
            )

        if (needVersion.toInt() > getVersion(this)) {
            val appPackageName = packageName
            val dialog = ErrorDialogFragment(
                onAcceptClickLister = { openPlayStore(this, appPackageName) },
                error
            )
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    /**
     * Initializes the NavController for navigation between fragments.
     */
    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.isConnected()
    }
}