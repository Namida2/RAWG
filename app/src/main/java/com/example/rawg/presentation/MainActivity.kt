package com.example.rawg.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.interfaces.NavigationCallback
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.prepareFadeInAnimation
import com.example.featureGameDetails.presentation.GameDetailsFragment
import com.example.featureGamesViewPager.domain.di.GamesViewPagerDepsStore
import com.example.featureGamesViewPager.presentation.GamesViewPagerFragment
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.ViewModelFactory
import com.example.rawg.domain.tools.appComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: App a GamesViewPagerFragment, GameScreenDetails and like games //STOPPED//
class MainActivity : AppCompatActivity(), NavigationCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        getViewModel()
        observeViewModelStates()
        viewModel.readFilters()
    }

    private fun getViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(appComponent)
        )[MainViewModel::class.java]
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(this) {
            when (it) {
                is MainVMStates.LostNetworkConnection ->
                    createMessageAlertDialog(checkNetworkConnectionMessage)
                        ?.show(supportFragmentManager, "")
                is MainVMStates.FiltersLoadedSuccessfully -> {
                    GamesViewPagerDepsStore.navigationCallback = this
                    supportFragmentManager.beginTransaction()
                        .replace(binding.root.id, GamesViewPagerFragment()).commitNow()
                }
                is MainVMStates.Error -> {
                    createMessageAlertDialog(it.message)
                        ?.show(supportFragmentManager, "")
                }
                MainVMStates.ReadingFilters -> {}
                is MainVMStates.Default -> {}
            }
        }
    }

    override fun navigateTo(destination: Fragment) {
        logD("navigateTo")
        supportFragmentManager.beginTransaction()
            .replace(binding.root.id, destination)
            .addToBackStack("")
            .commit()
    }

}

