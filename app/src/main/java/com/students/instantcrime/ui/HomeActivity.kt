package com.students.instantcrime.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.R
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.enums.Role
import com.students.instantcrime.data.models.User
import com.students.instantcrime.ui.auth.LoginActivity

private const val TAG = "HomtActivity"

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var user: User

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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {


        if (Firebase.auth.currentUser != null) {

            menu?.removeItem(R.id.login_menu_item)

            if (this::user.isInitialized) {

                when (Role.fromString(user.role!!)) {
                    Role.Admin -> {
                        menu?.removeItem(R.id.officer_dashboard_menu_item)
                    }

                    Role.Officer -> {
                        menu?.removeItem(R.id.admin_dashboard_menu_item)
                    }

                    else -> {
                        menu?.removeItem(R.id.admin_dashboard_menu_item)
                        menu?.removeItem(R.id.officer_dashboard_menu_item)
                    }
                }

            } else {
                menu?.removeItem(R.id.admin_dashboard_menu_item)
                menu?.removeItem(R.id.officer_dashboard_menu_item)
            }

        } else {
            menu?.removeItem(R.id.logout_menu_item)
            menu?.removeItem(R.id.admin_dashboard_menu_item)
            menu?.removeItem(R.id.officer_dashboard_menu_item)
            menu?.removeItem(R.id.my_reports_menu_item)
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.login_menu_item -> {

                sendToLogin()

                true
            }

            R.id.logout_menu_item -> {

                logoutUser()

                true
            }

            R.id.officer_dashboard_menu_item -> {

                true
            }

            R.id.admin_dashboard_menu_item -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun logoutUser() {

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Signing Out")
            .setMessage("Are you sure you want to logout from the application")
            .setPositiveButton("Sure") { dialog, _ ->
                Firebase.auth.signOut()
                invalidateOptionsMenu()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        alertDialog.show()
    }

    private fun sendToLogin() {

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        invalidateOptionsMenu()

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {

            //Check current user profile and check level
            Firebase.firestore.collection(Constants.USERS_ROOT)
                .document(currentUser.uid)
                .addSnapshotListener(this) { documentSnapshot, error ->

                    if (error != null) {
                        Log.e(TAG, "onStart: ", error)
                        return@addSnapshotListener
                    }

                    documentSnapshot?.let {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User::class.java)!!

                            invalidateOptionsMenu()
                        }
                    }
                }
        }
    }

}