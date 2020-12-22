package skalii.testjob.trackensure.service


import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData

import javax.inject.Inject

import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.ui.activity.SaveRefillActivity


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
        SaveRefillActivity.getViewModelComponent().injectService(this)
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE", "ModelsSaverService.onCreate()")
    }

    @Suppress("UNREACHABLE_CODE", "UNCHECKED_CAST")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICE", "ModelsSaverService.onStartCommand()")

        if (intent != null) {

            val gasStation =
                intent.getSerializableExtra("gas_station") as GasStation? ?: GasStation()
            val supplier = intent.getSerializableExtra("supplier") as Supplier? ?: Supplier()
            val refill = intent.getSerializableExtra("refill") as Refill? ?: Refill()

            val connectionCheckerIntent = Intent(this, ConnectionCheckerService::class.java)
            startService(connectionCheckerIntent)

            ConnectionCheckerService.getIsConnectLiveData().observe(this, {
                Log.d("SERVICE", "ConnectionCheckerService.isConnectLiveData() = $it")
                if (it) {
                    stopService(connectionCheckerIntent)

                    gasStationViewModel.runSync(gasStation, { id ->
                        refill.idGasStation = id
                        refreshDataProgression("gas_station", true)
                    }, { }, gasStation.id == 0)

                    supplierViewModel.runSync(supplier, { id ->
                        refill.idSupplier = id
                        refreshDataProgression("supplier", true)
                    }, { }, supplier.id == 0)

                    ConnectionCheckerService.getIsConnectLiveData().removeObservers(this)
                }
            })

            dataProgressionLiveData.observe(this, { list ->
                list.forEach { value ->
                    Log.d(
                        "SERVICE",
                        "ModelsSaverService().dataProgressionLiveData(\"${value.key}\") = ${value.value}"
                    )
                }
                if (!list.containsValue(false)) stopSelf()
                else if (list["gas_station"] == true && list["supplier"] == true) {
                    refillViewModel.runSync(refill, {
                        refreshDataProgression("refill", true)
                    }, { }, refill.id == 0)
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