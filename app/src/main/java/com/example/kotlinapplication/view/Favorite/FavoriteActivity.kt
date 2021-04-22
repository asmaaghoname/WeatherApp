package com.example.kotlinapplication.view.Favorite

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.roomDataSource.FavoriteEntity
import com.example.kotlinapplication.view.Alert.AlertActivity
import com.example.kotlinapplication.view.SettingsActivity
import com.example.kotlinapplication.viewModel.FavoriteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class FavoriteActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    lateinit var favoriteRecyclerView: RecyclerView

    private var favoriteRecyclerViewAdapter = FavoriteAdapter(arrayListOf())
    private lateinit var viewModel: FavoriteViewModel

    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

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



        favoriteRecyclerView = findViewById(R.id.FavoriteRecyclerView)
        val btn: FloatingActionButton = findViewById(R.id.addFavFloatingButton)
        btn.setOnClickListener {
            val intent = Intent(this, FavoriteMap::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        observeViewModel(viewModel)

        init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    private fun init(){
        favoriteRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = favoriteRecyclerViewAdapter
        }

    }

    private fun observeViewModel(viewModel: FavoriteViewModel) {
        viewModel.getFavoriteInfo().observe(this, Observer { updateUI(it) })
        viewModel.getError().observe(this, Observer { showError(it) })
    }

    private fun updateUI(data: List<FavoriteEntity>) {
        favoriteRecyclerViewAdapter.updateList(data,viewModel)
        println(data)
    }

    private fun showError(msg: String) {
        Log.i("call", msg)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sorry")
        builder.setMessage(msg)

        builder.setPositiveButton("OK") { _, _ ->
            Toast.makeText(
                    applicationContext,
                    "OK", Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
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