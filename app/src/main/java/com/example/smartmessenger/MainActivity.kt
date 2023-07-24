package com.example.smartmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.screens.authentication.SingInFragment
import com.example.smartmessenger.screens.authentication.SingUpFragment
import com.example.smartmessenger.screens.chatlist.ChatListFragment
import com.example.smartmessenger.screens.currentchat.CurrentChatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            navController = f.findNavController()
        }

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (f is SingInFragment || f is SingUpFragment)
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.auth_background_color)
            else if (f is ChatListFragment || f is CurrentChatFragment)
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.main_toolbar_color)


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Singletons.init(this)


        FirebaseApp.initializeApp(this)
        navStaff()//TODO

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        if (!Singletons.appSettings.getIsRememberUser())
            Singletons.appSettings.setCurrentUId(null)
    }

    private fun navStaff() {
        val navController = getRootNavController()
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        val uId = Singletons.appSettings.getCurrentUId()
        graph.setStartDestination(
            if (uId == null) R.id.singInFragment
            else R.id.chatListFragment)
        navController.graph = graph

    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

}