package skalii.testjob.trackensure


import android.app.Application

import skalii.testjob.trackensure.di.component.AppComponent
import skalii.testjob.trackensure.di.component.DaggerAppComponent
import skalii.testjob.trackensure.di.component.DaggerDataComponent
import skalii.testjob.trackensure.di.component.DataComponent
import skalii.testjob.trackensure.di.module.AppModule
import skalii.testjob.trackensure.di.module.LocalStorageModule


class TrackApplication : Application() {

    companion object {

        private lateinit var appComponent: AppComponent
        private lateinit var dataComponent: DataComponent

        fun getAppComponent() = appComponent
        fun getDataComponent() = dataComponent

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

        dataComponent = DaggerDataComponent
            .builder()
//            .localStorageModule(LocalStorageModule(this))
//            .remoteStorageModule(RemoteStorageModule())
            .build()

    }

}