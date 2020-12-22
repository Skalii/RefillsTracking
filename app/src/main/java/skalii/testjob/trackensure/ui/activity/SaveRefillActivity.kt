package skalii.testjob.trackensure.ui.activity


import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat

import by.kirich1409.viewbindingdelegate.viewBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ActivitySaveRefillBinding
import skalii.testjob.trackensure.di.component.DaggerViewModelComponent
import skalii.testjob.trackensure.di.component.ViewModelComponent
import skalii.testjob.trackensure.di.module.ViewModelModule
import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.getDateTimeFormatter
import skalii.testjob.trackensure.helper.isPermissionGranted
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.helper.toast
import skalii.testjob.trackensure.helper.type.FuelType
import skalii.testjob.trackensure.service.ModelsSaverService


class SaveRefillActivity :
    AppCompatActivity(R.layout.activity_save_refill),
    OnMapReadyCallback {

    private val viewBinding
            by viewBinding(ActivitySaveRefillBinding::bind, R.id.activity_save_refill)
    private lateinit var map: GoogleMap
    private var currentGeopoint = Pair(0.00, 0.00)
    private lateinit var currentLocation: LatLng

    private val refillViewModel by viewModels<RefillViewModel>()
    private val gasStationViewModel by viewModels<GasStationViewModel>()
    private val supplierViewModel by viewModels<SupplierViewModel>()

    private lateinit var mapFragment: SupportMapFragment

    companion object {
        private lateinit var viewModelComponent: ViewModelComponent
        fun getViewModelComponent() = viewModelComponent
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ResourcesCompat.getColor(resources, R.color.color_accent, null))
        )

        refillViewModel.init(applicationContext)
        gasStationViewModel.init(applicationContext)
        supplierViewModel.init(applicationContext)

        viewModelComponent = DaggerViewModelComponent
            .builder()
            .viewModelModule(
                ViewModelModule(refillViewModel, gasStationViewModel, supplierViewModel)
            )
            .build()

        val buttonSave = viewBinding.createRefill.buttonSaveFragmentSaveRefill
        val buttonDateTimeRefresh =
            viewBinding.createRefill.buttonDateTimeRefreshFragmentSaveRefill
        val editDate = viewBinding.createRefill.editDateFragmentSaveRefill
        val editGasStation = viewBinding.createRefill.autoGasStationFragmentSaveRefill
        val editSupplier = viewBinding.createRefill.autoFuelSupplierFragmentSaveRefill
        val editLiter = viewBinding.createRefill.editLiterFragmentSaveRefill
        val editCost = viewBinding.createRefill.editCostFragmentSaveRefill
        val spinnerFuelType = viewBinding.createRefill.spinnerFuelTypeFragmentSaveRefill
        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
            .also { it.getMapAsync(this) }


        gasStationViewModel.getLocal(true).observe(this, { gasStations ->
            editGasStation.setAdapter(ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                gasStations.map { it.title }
            ))
        })

        supplierViewModel.getLocal(true).observe(this, { suppliers ->
            editSupplier.setAdapter(ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suppliers.map { it.name }
            ))
        })

        spinnerFuelType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            FuelType
                .values()
                .filterNot { it == FuelType.EMPTY || it == FuelType.UNKNOWN }
                .map { it.value }
        )
        buttonDateTimeRefresh.setOnClickListener {
            editDate.setText(LocalDateTime.now().format(getDateTimeFormatter()))
        }

        var gasStation = GasStation()
        var supplier = Supplier()
        var refill = Refill()

        if (this.intent.extras?.containsKey("refill") == true) {
            gasStation =
                intent.getSerializableExtra("gas_station") as GasStation? ?: GasStation()
            supplier = intent.getSerializableExtra("supplier") as Supplier? ?: Supplier()
            refill = intent.getSerializableExtra("refill") as Refill? ?: Refill()

            Log.d("EXISTS", refill.toString())

            GlobalScope.launch(Dispatchers.Main) {
                refreshCurrentLocation(map.apply { clear() }, gasStation.geopoint)
            }

            editDate.apply {
                setText(refill.date.format(getDateTimeFormatter()))

                isEnabled = true
                inputType = InputType.TYPE_NULL

                setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) callOnClick()
                }

                setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val date =
                        OnDateSetListener { v: DatePicker?, year: Int, month: Int, day: Int ->
                            calendar[year, month] = day
                            setText(
                                LocalDateTime
                                    .ofInstant(calendar.time.toInstant(), ZoneId.systemDefault())
                                    .format(getDateTimeFormatter())
                            )
                        }

                    DatePickerDialog(
                        context, date,
                        calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                        calendar[Calendar.DAY_OF_MONTH]
                    ).show()

                }
            }
            editGasStation.setText(gasStation.title)
            editSupplier.setText(supplier.name)
            editLiter.setText(refill.liter.toString())
            editCost.setText(refill.cost.toString())
            spinnerFuelType.setSelection(
                @Suppress("UNCHECKED_CAST")
                (spinnerFuelType.adapter as ArrayAdapter<String>).getPosition(refill.fuelType.value)
            )

        } else {
            editDate.setText(LocalDateTime.now().format(getDateTimeFormatter()))
        }


        buttonSave.setOnClickListener {

            gasStation.title =
                if (editGasStation.text.toString().isNotBlank())
                    editGasStation.text.toString().trim()
                else "Unknown gas station"
            gasStation.geopoint = currentGeopoint
            gasStation.id =
                if (gasStationViewModel.checkExists("title", gasStation.title)) {
                    gasStationViewModel.getFinalLocal(gasStation.title)?.id ?: 0
                } else 0

            supplier.name =
                if (editSupplier.text.toString().isNotBlank())
                    editSupplier.text.toString().trim()
                else "Unknown supplier"
            supplier.id =
                if (supplierViewModel.checkExists("name", supplier.name)) {
                    supplierViewModel.getFinalLocal(supplier.name)?.id ?: 0
                } else 0

            refill.date = LocalDateTime.parse(editDate.text.toString(), getDateTimeFormatter())
            refill.liter =
                if (editLiter.text.toString().isNotBlank())
                    editLiter.text.toString().toDouble()
                else 0.00
            refill.cost =
                if (editCost.text.toString().isNotBlank())
                    editCost.text.toString().toDouble()
                else 0.00
            refill.fuelType = FuelType.toEnum(spinnerFuelType.selectedItem.toString())
            refill.idGasStation = gasStation.id
            refill.idSupplier = supplier.id
            refill.uid = FirebaseAuth.getInstance().uid ?: "a0WgcHUYP7gRHQapRT8st3R5Cde2"

            startService(
                Intent(this, ModelsSaverService::class.java).apply {
                    putExtra("gas_station", gasStation)
                    putExtra("supplier", supplier)
                    putExtra("refill", refill)
                }
            )
            finish()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        mapFragment.view?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        /*if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { }*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        checkPermissions()

        map.setOnMapClickListener { location ->
            val markerOptions = MarkerOptions().also { options ->
                options.position(location)
                options.title(location.latitude.toString() + " : " + location.longitude)
                currentGeopoint = Pair(location.latitude, location.longitude)
            }
            map.clear()
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
            map.addMarker(markerOptions)
        }
    }

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
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 50)
        else if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 51)
        else refreshCurrentLocation(map)
    }

    private fun refreshCurrentLocation(
        googleMap: GoogleMap,
        manually: Pair<Double, Double>? = null
    ) {
        setCurrentLocation(manually)
        currentLocation = LatLng(currentGeopoint.first, currentGeopoint.second)

        val currentMarker = MarkerOptions().also { options ->
            options.position(currentLocation)
            options.title(currentLocation.latitude.toString() + " : " + currentLocation.longitude)
        }

        googleMap.addMarker(currentMarker)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15F))
    }

    private fun setCurrentLocation(manually: Pair<Double, Double>? = null) {
        currentGeopoint = if (manually != null) manually
        else {
            val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissions()
                return
            }
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            when {
                gpsLocation != null -> Pair(gpsLocation.latitude, gpsLocation.longitude)
                netLocation != null -> Pair(netLocation.latitude, netLocation.longitude)
                else -> currentGeopoint
            }
        }
    }

}