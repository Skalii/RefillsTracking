package skalii.testjob.trackensure.ui.activity


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat

import by.kirich1409.viewbindingdelegate.viewBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ActivityCreateRefillBinding
import skalii.testjob.trackensure.helper.isPermissionGranted
import skalii.testjob.trackensure.helper.toast


class CreateRefillActivity :
    AppCompatActivity(R.layout.activity_create_refill),
    OnMapReadyCallback {

    private val viewBinding
            by viewBinding(ActivityCreateRefillBinding::bind, R.id.activity_create_refill)
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ResourcesCompat.getColor(resources, R.color.color_accent, null))
        )

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        refreshCurrentLocation(map)

        map.setOnMapClickListener {
            val markerOptions = MarkerOptions().also { options ->
                options.position(it)
                options.title(it.latitude.toString() + " : " + it.longitude)
            }
            map.clear()
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10F));
            googleMap.addMarker(markerOptions)
        }
    }

    private var currentGeopoint = Pair(0.00, 0.00)
    private lateinit var currentLocation: LatLng

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            50, 51 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation()
                } else {
                    applicationContext.toast("Need access permissions")
                }
                return
            }
        }
    }


    private fun checkPermissions() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            50
        )
        else if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            51
        )
        else setCurrentLocation()
    }

    private fun setCurrentLocation() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        currentGeopoint = when {
            gpsLocation != null -> Pair(gpsLocation.latitude, gpsLocation.longitude)
            netLocation != null -> Pair(netLocation.latitude, netLocation.longitude)
            else -> currentGeopoint
        }
    }

    private fun refreshCurrentLocation(googleMap: GoogleMap) {
        setCurrentLocation()
        currentLocation = LatLng(currentGeopoint.first, currentGeopoint.second)

        val currentMarker = MarkerOptions().also { options ->
            options.position(currentLocation)
            options.title(currentLocation.latitude.toString() + " : " + currentLocation.longitude)
        }

        googleMap.addMarker(currentMarker)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15F))

    }

}