package com.example.kotlinapplication.view.Alert

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import android.util.Log
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.roomDataSource.AlertEntity
import com.example.kotlinapplication.view.Favorite.FavoriteActivity
import com.example.kotlinapplication.view.SettingsActivity
import com.example.kotlinapplication.viewModel.AlertViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_alert.*
import java.util.*

class AlertActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var alertRecyclerView: RecyclerView
    private var alertRecyclerViewAdapter = AlertRecyclerViewAdapter(arrayListOf())
    private lateinit var viewModel: AlertViewModel
    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)



        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val navigationView: NavigationView = findViewById(R.id.
        nav_view)
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        alertRecyclerView = findViewById(R.id.alertRecyclerView)

        val btn: FloatingActionButton = findViewById(R.id.addFloatingButton)
        btn.setOnClickListener {
            val intent = Intent(this, AddAlert::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(AlertViewModel::class.java)
        observeViewModel(viewModel)

        initRecyclerViewList()

        val calendar = Calendar.getInstance()
        val currentDay = "${calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)}"
        println("********************************* $currentDay")
    }
    override fun onResume() {
        super.onResume()
         viewModel.getAll()
    }

    private fun initRecyclerViewList(){
        alertRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = alertRecyclerViewAdapter
        }

    }

    private fun observeViewModel(viewModel: AlertViewModel) {
        viewModel.getAlertInfo().observe(this, Observer { updateUI(it) })


    }

    private fun updateUI(data: List<AlertEntity>) {

        alertRecyclerViewAdapter.updateList(data,viewModel)
        println(data)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(
                    Intent(
                            this,
                            HomeActivity::class.java
                    )
            )
            R.id.nav_setting -> startActivity(
                    Intent(
                            this,
                            SettingsActivity::class.java
                    )
            )
            R.id.nav_fav -> startActivity(
                    Intent(
                            this,
                            FavoriteActivity::class.java
                    )
            )
            R.id.nav_alert -> startActivity(
                    Intent(
                            this,
                            AlertActivity::class.java
                    )
            )

        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true    }
    }
