package com.example.kotlinapplication.view

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.*
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import com.example.kotlinapplication.view.Alert.AlertActivity
import com.example.kotlinapplication.view.Favorite.FavoriteActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.navigation.NavigationView
import java.util.*

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {


    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var locationListPreference: ListPreference
        private lateinit var locationEditTextPreference: EditTextPreference
        private lateinit var unitListPreference: ListPreference
        private lateinit var languageListPreference: ListPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            Places.initialize(requireContext(), "AIzaSyD1a36wBw4n7Jhjef3dT00W9R7hHOn_Cy0")
            val fieldList = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(requireContext())
            val pref = PreferenceManager.getDefaultSharedPreferences(context)

            locationListPreference = findPreference("location")!!
            unitListPreference = findPreference("unit")!!
            languageListPreference = findPreference("language")!!
            locationEditTextPreference = findPreference("location_address")!!
            locationEditTextPreference.isVisible = !pref.getBoolean("currentLocation", true)

            locationListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())

                if(value.toString() == "other"){
                    startActivityForResult(intent, 100)
                }else{
                    pref.edit().putBoolean("currentLocation", true).apply()
                    locationEditTextPreference.isVisible = false
                }
                true
            }

            unitListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())
                pref.edit().putString("unit", value.toString()).apply()
                true
            }

            languageListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())
                pref.edit().putString("language", value.toString()).apply()
                setLang(value.toString())
                true
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == 100) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val place = Autocomplete.getPlaceFromIntent(data)
                            locationEditTextPreference.text = place.address
                            locationEditTextPreference.isVisible = true

                            val pref = PreferenceManager.getDefaultSharedPreferences(context)
                            pref.edit().putFloat("lat", place.latLng!!.latitude.toFloat()).apply()
                            pref.edit().putFloat("lon", place.latLng!!.longitude.toFloat()).apply()
                            pref.edit().putBoolean("currentLocation", false).apply()
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        // TODO: Handle the error.
                        data?.let {
                            val status = Autocomplete.getStatusFromIntent(data)
                        }
                    }
                    Activity.RESULT_CANCELED -> {

                    }
                }
                return
            }
            super.onActivityResult(requestCode, resultCode, data)
        }

        private fun setLang(code: String) {
            val locale = Locale(code)
            Locale.setDefault(locale)
            val resources: Resources = requireContext().resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
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
        return true
      }
    }
