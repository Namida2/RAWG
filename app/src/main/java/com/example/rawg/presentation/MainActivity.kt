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

//
//data class Name (
//    var firstName: String = "",
//    var secondName: String = ""
//) {
//    var other: String = ""
//    operator fun component3() = other
//}
//
//abstract class A {
//    constructor( name: String) {
//
//    }
//    constructor( name: String, age: Int) {
//
//    }
//    abstract fun fromA()
//}
//
//class Test private constructor(name: String): A(name) {
//    constructor(name: String, age: Int): this(name)
//    private fun doMagic() {
//        println("private")
//    }
//    fun public() {
//        println("public")
//    }
//    override fun fromA() {
//        println("override")
//    }
//    fun fromA(string: String) {
//        println("override")
//    }
//    companion object test {
//
//    }
//}
//
//fun interface Printer {
//    fun print()
//}
//
//fun isOdd(x: Int) = x % 2 != 0
//
//class Ext {
//    var name: String = ""
//        get() = field
//    set(value) { field = value }
//
//    private fun somePrivateMethod() {}
//    fun somePublicMethod() {}
//
//    @JvmInline
//    value class Password(private val sr: Password1) {
//        init {
//
//        }
//
//        fun dosd() {
//            println("+++++++=")
//        }
//
//        var aaaa: Int
//            get() = 1
//            set(value) {}
//    }
//
//    @JvmInline
//    value class Password1(private val sr: Ext) {
//        init {
//
//        }
//
//        fun dosd() {
//            println("+++++++=")
//        }
//
//        var aaaa: Int
//            get() = 1
//            set(value) {}
//    }
//
//}
//
//var name: String = ""
//    get() = ""
//set(value) {field = value + ""}
//
//@JvmInline
//value class Password(private val s: String)
//
//
//class MyMap<K, V>(
//    val map: MutableMap<K, V> = mutableMapOf()
//): HashMap<K, V>() {
//
//}
//
fun main() {
    val name by lazy(){
        ""
    }

}
//
//class Resource
//
//class Owner {
//    var varResource: Resource by ResourceDelegate()
//}
//
//class ResourceDelegate(private var resource: Resource = Resource()) {
//    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
//        return resource
//    }
//    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: Resource) {
//        resource = value
//    }
//
//}
//
//object Name0: Name1() {
//    val name: String by lazy() {
//        "asd"
//    }
//    val obs: String by Delegates.observable("") { prop, old, new ->
//
//    }
//    val муе: String by Delegates.vetoable("") { property: KProperty<*>, oldValue: String, newValue: String ->
//
//        false || true
//    }
//    override fun printX() {
//        super.printX()
//    }
//}
//
//open class Name1 {
//    private fun getObject() = object {
//        val x: String = "x"
//    }
//
//    open fun printX() {
//        println(getObject().x)
//    }
//}

