package skalii.testjob.trackensure


import android.app.Application

import skalii.testjob.trackensure.di.component.AppComponent
import skalii.testjob.trackensure.di.component.DaggerAppComponent
import skalii.testjob.trackensure.di.module.AppModule


class TrackApplication : Application() {

    companion object {

        private lateinit var appComponent: AppComponent

        fun getAppComponent() = appComponent

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

    }

}