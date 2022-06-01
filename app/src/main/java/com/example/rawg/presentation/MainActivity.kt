package com.example.rawg.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.extensions.createMessageAlertDialog
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.interfaces.NavigationCallback
import com.example.featureGamesViewPager.domain.di.GamesViewPagerDepsStore
import com.example.featureGamesViewPager.presentation.GamesViewPagerFragment
import com.example.featureGamesViewPager.presentation.GamesViewPagerFragment.Companion.GAMES_VIEW_PAGER_FRAGMENT_TAG
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.ViewModelFactory
import com.example.rawg.domain.tools.appComponent
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity(), NavigationCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GamesViewPagerDepsStore.navigationCallback = this
        if (supportFragmentManager.findFragmentByTag(GAMES_VIEW_PAGER_FRAGMENT_TAG) != null) return
        observeViewModelStates()
        viewModel.readFiltersAndMyLikes()

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
                    supportFragmentManager.beginTransaction().replace(
                        binding.root.id, GamesViewPagerFragment(), GAMES_VIEW_PAGER_FRAGMENT_TAG
                    ).commitNow()
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

    override fun navigateTo(destination: Fragment, sharedEView: View?, tag: String) {
        logD("navigateTo")
        supportFragmentManager.beginTransaction()
            .replace(binding.root.id, destination)
            .addToBackStack("")
            .also { transaction ->
                sharedEView?.let { transaction.addSharedElement(it, tag) }
            }.commit()
    }

}

