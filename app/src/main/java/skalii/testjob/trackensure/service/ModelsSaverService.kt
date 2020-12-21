package skalii.testjob.trackensure.service


import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData

import java.time.LocalDateTime

import javax.inject.Inject

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.helper.type.FuelType
import skalii.testjob.trackensure.ui.activity.CreateRefillActivity


class ModelsSaverService : LifecycleService() {

    @Inject
    protected lateinit var refillViewModel: RefillViewModel

    @Inject
    protected lateinit var gasStationViewModel: GasStationViewModel

    @Inject
    protected lateinit var supplierViewModel: SupplierViewModel

    private val dataProgressionLiveData = MutableLiveData<MutableMap<String, Boolean>>()
    private val dataLoadingProgressTags = mutableMapOf(
        "refill" to false, "gas_station" to false, "supplier" to false
    )

    private fun refreshDataProgression(tag: String, value: Boolean) {
        dataProgressionLiveData.postValue(dataLoadingProgressTags.apply { this[tag] = value })
    }


    init {
        CreateRefillActivity.getViewModelComponent().injectService(this)
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE", "ModelsSaverService.onCreate()")
    }

    @ExperimentalSerializationApi
    @Suppress("UNREACHABLE_CODE", "UNCHECKED_CAST")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICE", "ModelsSaverService.onStartCommand()")

        if (intent != null) {

            val date = intent.getSerializableExtra("date") as LocalDateTime
            val liter = intent.getDoubleExtra("liter", 0.00)
            val cost = intent.getDoubleExtra("cost", 0.00)
            val fuelType = intent.getSerializableExtra("fuelType") as FuelType
            val gasStationTitle = intent.getStringExtra("gasStationTitle") ?: "Unknown gas station"
            val geopoint = intent.getSerializableExtra("geopoint") as Pair<Double, Double>
            val supplierName = intent.getStringExtra("supplierName") ?: "Unknown supplier"
            val uid = intent.getStringExtra("uid") ?: "a0WgcHUYP7gRHQapRT8st3R5Cde2"


            val gasStation = GasStation(0, gasStationTitle, geopoint)
            gasStationViewModel.runSync(gasStation, { idGasStation ->
                gasStation.id = idGasStation
                refreshDataProgression("gas_station", true)
            }, { })

            val supplier = Supplier(0, supplierName)
            supplierViewModel.runSync(supplier, { idSupplier ->
                supplier.id = idSupplier
                refreshDataProgression("supplier", true)
            }, { })


            dataProgressionLiveData.observe(this, { list ->
                list.forEach {
                    Log.d(
                        "SERVICE",
                        "ModelsSaverService().dataProgressionLiveData(\"${it.key}\") = ${it.value}"
                    )
                }
                if (!list.containsValue(false)) stopSelf()
                else if (list["gas_station"] == true && list["supplier"] == true) {
                    refillViewModel.runSync(
                        Refill(0, date, liter, cost, fuelType, gasStation.id, supplier.id, uid),
                        { refreshDataProgression("refill", true) },
                        { })
                }
            })
        } else stopSelf()

        return super.onStartCommand(intent, flags, startId) //START_STICKY
    }

    override fun onDestroy() {
        Log.d("SERVICE", "ModelsSaverService.onDestroy()")
        dataProgressionLiveData.removeObservers(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d("SERVICE", "ModelsSaverService.onBind()")
        throw UnsupportedOperationException("Not yet implemented")
    }

}