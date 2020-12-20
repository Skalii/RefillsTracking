package skalii.testjob.trackensure.ui.activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.widget.ArrayAdapter

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider

import by.kirich1409.viewbindingdelegate.viewBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

import java.time.LocalDateTime

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ActivityCreateRefillBinding
import skalii.testjob.trackensure.di.component.DaggerViewModelComponent
import skalii.testjob.trackensure.di.component.ViewModelComponent
import skalii.testjob.trackensure.di.module.ViewModelModule
import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.getDateTimeFormatter
import skalii.testjob.trackensure.helper.isPermissionGranted
import skalii.testjob.trackensure.helper.toast
import skalii.testjob.trackensure.helper.type.FuelType
import skalii.testjob.trackensure.service.ModelsSaverService


class CreateRefillActivity :
    AppCompatActivity(R.layout.activity_create_refill),
    OnMapReadyCallback {

    private val viewBinding
            by viewBinding(ActivityCreateRefillBinding::bind, R.id.activity_create_refill)
    private lateinit var map: GoogleMap

    private lateinit var refillViewModel: RefillViewModel
    private lateinit var gasStationViewModel: GasStationViewModel
    private lateinit var supplierViewModel: SupplierViewModel


    companion object {

        private lateinit var viewModelComponent: ViewModelComponent

        fun getViewModelComponent() = viewModelComponent

    }


    @SuppressLint("RestrictedApi")
    @ExperimentalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ResourcesCompat.getColor(resources, R.color.color_accent, null))
        )

        refillViewModel = ViewModelProvider(this)[RefillViewModel::class.java]
            .also { it.init(applicationContext) }
        gasStationViewModel = ViewModelProvider(this)[GasStationViewModel::class.java]
            .also { it.init(applicationContext) }
        supplierViewModel = ViewModelProvider(this)[SupplierViewModel::class.java]
            .also { it.init(applicationContext) }

        viewModelComponent = DaggerViewModelComponent
            .builder()
            .viewModelModule(
                ViewModelModule(refillViewModel, gasStationViewModel, supplierViewModel)
            )
            .build()

        val buttonSave = viewBinding.createRefill.buttonSaveFragmentCreateRefill
        val buttonDateTimeRefresh =
            viewBinding.createRefill.buttonDateTimeRefreshFragmentCreateRefill
        val spinnerFuelType = viewBinding.createRefill.spinnerFuelTypeFragmentCreateRefill
        val editDate = viewBinding.createRefill.editDateFragmentCreateRefill
        val editGasStation = viewBinding.createRefill.editGasStationFragmentCreateRefill
        val editSupplier = viewBinding.createRefill.editFuelSupplierFragmentCreateRefill
        val editLiter = viewBinding.createRefill.editLiterFragmentCreateRefill
        val editCost = viewBinding.createRefill.editCostFragmentCreateRefill
        val mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
            .also { it.getMapAsync(this) }

        spinnerFuelType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            FuelType
                .values()
                .filterNot { it == FuelType.EMPTY || it == FuelType.UNKNOWN }
                .map { it.value }
        )
        editDate.setText(LocalDateTime.now().format(getDateTimeFormatter()))
        buttonDateTimeRefresh.setOnClickListener {
            editDate.setText(LocalDateTime.now().format(getDateTimeFormatter()))
        }


        buttonSave.setOnClickListener {

            val date = LocalDateTime.parse(editDate.text.toString(), getDateTimeFormatter())

            val liter =
                if (editLiter.text.toString().isNotBlank()
                    && editLiter.text.toString().isNotEmpty()
                ) editLiter.text.toString().toDouble()
                else 0.00

            val cost =
                if (editCost.text.toString().isNotBlank()
                    && editCost.text.toString().isNotEmpty()
                ) editCost.text.toString().toDouble()
                else 0.00

            val fuelType = FuelType.toEnum(spinnerFuelType.selectedItem.toString())

            val gasStationTitle =
                if (editGasStation.text.toString().isNotBlank()
                    && editGasStation.text.toString().isNotEmpty()
                ) editGasStation.text.toString()
                else "Unknown gas station"

            val geopoint = currentGeopoint

            val supplierName =
                if (editSupplier.text.toString().isNotBlank()
                    && editSupplier.text.toString().isNotEmpty()
                ) editSupplier.text.toString()
                else "Unknown supplier"

            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val intent = Intent(this, ModelsSaverService::class.java).also {
                it.putExtra("date", date)
                it.putExtra("liter", liter)
                it.putExtra("cost", cost)
                it.putExtra("fuelType", fuelType)
                it.putExtra("gasStationTitle", gasStationTitle)
                it.putExtra("geopoint", geopoint)
                it.putExtra("supplierName", supplierName)
                it.putExtra("uid", uid)
            }

            startService(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        checkPermissions()

        map.setOnMapClickListener {
            val markerOptions = MarkerOptions().also { options ->
                options.position(it)
                options.title(it.latitude.toString() + " : " + it.longitude)
                currentGeopoint = Pair(it.latitude, it.longitude)
            }
            map.clear()
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15F));
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
                    refreshCurrentLocation(map)
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
        else refreshCurrentLocation(map)
    }

    private fun setCurrentLocation() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
            return
        }
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15F))

    }

}