package com.example.smartmessenger.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.R
import com.example.smartmessenger.data.settings.AppSettings
import com.example.smartmessenger.presentation.authentication.SingInFragment
import com.example.smartmessenger.presentation.authentication.SingUpFragment
import com.example.smartmessenger.presentation.chatlist.ChatListFragment
import com.example.smartmessenger.presentation.currentchat.CurrentChatFragment
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    @Inject lateinit var appSettings: AppSettings

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            navController = f.findNavController()
        }

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (f is SingInFragment || f is SingUpFragment)
                window.statusBarColor = ContextCompat.getColor(this@MainActivity,
                    R.color.auth_background_color
                )
            else if (f is ChatListFragment || f is CurrentChatFragment)
                window.statusBarColor = ContextCompat.getColor(this@MainActivity,
                    R.color.main_toolbar_color
                )


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navStaff()//TODO

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        if (!appSettings.getIsRememberUser())
            appSettings.setCurrentUId(null)
    }

    private fun navStaff() {
        val navController = getRootNavController()
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        val uId = appSettings.getCurrentUId()
        graph.setStartDestination(
            if (uId == null) R.id.singInFragment
            else R.id.chatListFragment
        )
        navController.graph = graph

    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

}