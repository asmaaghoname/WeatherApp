package com.example.kotlinapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinapplication.model.Daily
import com.example.kotlinapplication.model.Hourly
import com.example.kotlinapplication.model.WeatherData
import com.example.kotlinapplication.view.Alert.AlertActivity
import com.example.kotlinapplication.view.Alert.AlertBroadcast
import com.example.kotlinapplication.view.Alert.AlertService
import com.example.kotlinapplication.view.DailyListAdapter
import com.example.kotlinapplication.view.Favorite.FavoriteActivity
import com.example.kotlinapplication.view.HourlyListAdapter
import com.example.kotlinapplication.view.SettingsActivity
import com.example.kotlinapplication.viewModel.HomeViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dailyListAdapter = DailyListAdapter(arrayListOf())
    var hourlyListAdapter = HourlyListAdapter(arrayListOf())
    lateinit var viewModel : HomeViewModel
    private lateinit var loading: ProgressBar
    private var source = "home"
    private val myReceiver = AlertBroadcast()

    private var lat = 0.0F
    private var lon = 0.0F
    private var unit = String()
    private var language = String()


    var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loading = findViewById(R.id.loading_view)
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


        // end of drawer******************************************

        val recyclerViewForDaily: RecyclerView = findViewById(R.id.recycler_view_daily)
        recyclerViewForDaily.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL, true)
        recyclerViewForDaily.adapter = dailyListAdapter

        val recyclerViewForHourly: RecyclerView = findViewById(R.id.recycler_view_hourly)
        recyclerViewForHourly.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL, true)
        recyclerViewForHourly.adapter = hourlyListAdapter


        Log.i("TAG", "home: ")
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
         viewModel.WeatherLiveData.observe(this, Observer { data ->
            data?.let {
                data.let { updateMainDetails(it) }
                data.hourly?.let { it1 -> updateHourlyListUI(it1) }
                data.daily?.let { it1 -> updateDailyListUI(it1) }
                data.let { updateDetailsLayout(it) }
            }
        })
        viewModel.loadingLiveData.observe(this,Observer { showLoading(it) })



        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
        registerReceiver(myReceiver, intentFilter)


        val intent = Intent(this, AlertService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //startForegroundService(intent)
           // startService(intent)
        }else{
            startService(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(this)) {
            askPermission()
        }

    }

    fun getIcon(icon: String): String {
        return "http://openweathermap.org/img/w/${icon}.png"
    }

    private fun updateDailyListUI(it: List<Daily>) {
        dailyListAdapter.updateHoursList(it)
    }
    private fun updateHourlyListUI(it: List<Hourly>) {
        hourlyListAdapter.updateHoursList(it)
    }

    private fun updateDetailsLayout(it: WeatherData) {

//        data_windSpeed.text= it.current?.windSpeed.toString()
//        data_cloudCover.text=it.current?.clouds.toString()
        data_humidity.text=it.current?.humidity.toString()
        data_visibility.text=it.current?.visibility.toString()
        data_pressure.text=it.current?.pressure.toString()
        feels_like.text= it.current?.feelsLike.toString()
    }


    @SuppressLint("SetTextI18n")
    private fun updateMainDetails(it: WeatherData) {
        city.text = it.timezone?.substringAfter("/")
        val calendar = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault())
        val currenTimeZone = (it.current?.dt?.toLong())?.times(1000)?.let { it1 -> Date(it1) }
        date_time.text = sdf.format(currenTimeZone)
        val options = RequestOptions()
            .error(R.mipmap.ic_launcher_round)
        val icon: String? = it.current?.weather?.get(0)?.icon
        this?.let { it1 ->
            Glide.with(it1)
                .setDefaultRequestOptions(options)
                .load(icon?.let { it2 -> getIcon(it2) })
                .into(imageView)

        }


        // first layout
        cloud_desc.text = it.current?.weather?.get(0)?.description
         max.text = getText(R.string.max).toString() + (it.daily?.get(0)?.temp?.max)?.toInt().toString()
         min.text = getText(R.string.min).toString() + (it.daily?.get(0)?.temp?.min)?.toInt().toString()
         windSpeed.text = getText(R.string.wind).toString() + (it.current?.windSpeed?.toInt()).toString()
         clouds.text = getText(R.string.clouds).toString()+ it.current?.clouds.toString() + "%"
        when (unit) {
            "imperial" ->
            {
                temp.text = it.current?.temp?.toInt().toString() +"${getText(R.string.fahrenheit)}"
                windSpeed.text = getText(R.string.wind).toString() + (it.current?.windSpeed?.toInt()).toString() + getText(R.string.ms)
            }
            "metric" ->{
                temp.text = it.current?.temp?.toInt().toString() +"${getText(R.string.celsius)}"
                windSpeed.text = getText(R.string.wind).toString() + (it.current?.windSpeed?.toInt()).toString() + getText(R.string.ms)
                 }
            else -> temp.text = it.current?.temp?.toInt().toString() +"${getText(R.string.kelvin)}"
        }

    }

    private fun askPermission() {
        AlertDialog.Builder(this)
                .setTitle("Permission")
                .setMessage("App need permission to display content overlay to ba able to use all its function")
                .setPositiveButton(
                        android.R.string.yes
                ) { dialog, which ->
                    val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + this.packageName)
                    )
                    startActivityForResult(intent, 0)
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun showLoading(flag: Boolean) {
        Log.i("call", flag.toString())
        if(flag){
            loading.visibility = View.VISIBLE
        }else{
            loading.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()
        if (intent.getStringExtra("source") != null) {
            source = intent.getStringExtra("source")!!
        }

        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        lat = pref.getFloat("lat", 0.0F)
        lon = pref.getFloat("lon", 0.0F)
        unit = pref.getString("unit", "metric")!!
        language = pref.getString("language", "en")!!
        val currentLocation = pref.getBoolean("currentLocation", true)
        if (source == "home") {
            if (currentLocation) {
                getLocation()
            } else {
                viewModel.fetchData(lat.toDouble(),lon.toDouble(),language,unit)
            }
        }else{
            viewModel.fetchData(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lon", 0.0),language,unit)
        }


    }

    private fun getLocation() {

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        val locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                val latitude = p0.latitude
                val longitude = p0.longitude

                Log.i("call", "Latitude: $latitude ; longitude: $longitude")
                val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val currentLocation = pref.getBoolean("currentLocation", true)
                if(currentLocation){
                    viewModel.fetchData(latitude,longitude,language,unit)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        100
                )
                return
            }
            locationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
            )
        } catch (ex: SecurityException) {
            Toast.makeText(applicationContext, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
    }


    // drawer
    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
        return true    }
}