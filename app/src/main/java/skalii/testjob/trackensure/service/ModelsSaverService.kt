package skalii.testjob.trackensure.service


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import java.time.LocalDateTime

import javax.inject.Inject

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.getDateTimeFormatter
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.helper.type.FuelType
import skalii.testjob.trackensure.ui.activity.CreateRefillActivity


class ModelsSaverService : Service() {

    @Inject
    protected lateinit var refillViewModel: RefillViewModel

    @Inject
    protected lateinit var gasStationViewModel: GasStationViewModel

    @Inject
    protected lateinit var supplierViewModel: SupplierViewModel


    init {
        CreateRefillActivity.getViewModelComponent().injectService(this)
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE", "ModelsSaverService.onCreate()")
    }

    @ExperimentalSerializationApi
    @Suppress("UNREACHABLE_CODE", "UNCHECKED_CAST")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("SERVICE", "ModelsSaverService.onStartCommand()")

        val date = intent.getSerializableExtra("date") as LocalDateTime
        val liter = intent.getDoubleExtra("liter", 0.00)
        val cost = intent.getDoubleExtra("cost", 0.00)
        val fuelType = intent.getSerializableExtra("fuelType") as FuelType
        val gasStationTitle = intent.getStringExtra("gasStationTitle") ?: "Unknown gas station"
        val geopoint = intent.getSerializableExtra("geopoint") as Pair<Double, Double>
        val supplierName = intent.getStringExtra("supplierName") ?: "Unknown supplier"
        val uid = intent.getStringExtra("uid") ?: "a0WgcHUYP7gRHQapRT8st3R5Cde2"

        Log.d("NEW_DATE", date.format(getDateTimeFormatter()))
        Log.d("NEW_LITER", liter.toString())
        Log.d("NEW_COST", cost.toString())
        Log.d("NEW_FUEL_TYPE", fuelType.value)
        Log.d("NEW_GAS_STATION_TITLE", gasStationTitle)
        Log.d("NEW_GEOPOINT", "[${geopoint.first}° N,${geopoint.second}° E]")
        Log.d("NEW_SUPPLIER_NAME", supplierName)
        Log.d("NEW_UID", uid)

        var idGasStation = gasStationViewModel.saveLocal(GasStation(gasStationTitle, geopoint))
        if (idGasStation == -1L) {
            idGasStation =
                gasStationViewModel.getFinalLocal(gasStationTitle, geopoint)[0].id.toLong()
        }
        Log.d("NEW_ID_GAS_STATION", idGasStation.toString())

        var idSupplier = supplierViewModel.saveLocal(Supplier(supplierName))
        if (idSupplier == -1L) {
            idSupplier = supplierViewModel.getFinalLocal(supplierName)[0].id.toLong()
        }
        Log.d("NEW_ID_SUPPLIER", idSupplier.toString())

        val idRefill = refillViewModel.saveLocal(
            Refill(date, liter, cost, fuelType, idGasStation.toInt(), idSupplier.toInt(), uid)
        )
        Log.d("NEW_ID_REFILL", idRefill.toString())


        Log.d("NEW_GAS_STATION", gasStationViewModel.getFinalLocal(idGasStation.toInt()).toString())
        Log.d("NEW_SUPPLIER", supplierViewModel.getFinalLocal(idSupplier.toInt()).toString())
        Log.d("NEW_REFILL", refillViewModel.getFinalLocal(idRefill.toInt()).toString())


        stopSelf()

        return super.onStartCommand(intent, flags, startId) //START_STICKY
    }

    override fun onDestroy() {
        Log.d("SERVICE", "ModelsSaverService.onDestroy()")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("SERVICE", "ModelsSaverService.onBind()")
        throw UnsupportedOperationException("Not yet implemented")
    }

}