package com.students.instantcrime.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.R
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.auth.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.defaultFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.login_menu_iten -> {

                sendToLogin()

                true
            }

            R.id.logout_menu_item -> {

                Firebase.auth.signOut()

                true
            }

            R.id.dashboard_menu_item -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun sendToLogin() {

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            toast("You're not authenticated")
        }
    }

}