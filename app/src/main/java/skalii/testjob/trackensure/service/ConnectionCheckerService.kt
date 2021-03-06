package skalii.testjob.trackensure.service


import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import skalii.testjob.trackensure.TrackApplication.Companion.getAppComponent
import skalii.testjob.trackensure.helper.isNetworkAvailable


class ConnectionCheckerService : LifecycleService() {

    companion object {
        private val isConnectLiveData = MutableLiveData<Boolean>()
        fun getIsConnectLiveData() = isConnectLiveData
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE", "ConnectionCheckerService.onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICE", "ConnectionCheckerService.onStartCommand()")

        GlobalScope.launch(Dispatchers.IO) {
            var isConnect = getAppComponent().getContext().isNetworkAvailable()
            isConnectLiveData.postValue(isConnect)
            if (!isConnect) {
                while (!isConnect) {
                    isConnect = getAppComponent().getContext().isNetworkAvailable()
                    isConnectLiveData.postValue(isConnect)
                    if (!isConnect) delay(5000)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId) //START_STICKY
    }

    override fun onDestroy() {
        Log.d("SERVICE", "ConnectionCheckerService.onDestroy()")
        isConnectLiveData.removeObservers(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d("SERVICE", "ConnectionCheckerService.onBind()")
        throw UnsupportedOperationException("Not yet implemented")
    }

}