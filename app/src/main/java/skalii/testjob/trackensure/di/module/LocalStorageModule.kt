package skalii.testjob.trackensure.di.module


import dagger.Module
import dagger.Provides

import javax.inject.Singleton

import skalii.testjob.trackensure.TrackApplication
//import skalii.testjob.trackensure.data.model.local.TrackDatabase


//@Module
class LocalStorageModule(private val application: TrackApplication) {

    /*@[Provides Singleton]
    fun provideLocalDatabase(): TrackDatabase =
        TrackDatabase.buildDatabase(application.applicationContext)*/

}