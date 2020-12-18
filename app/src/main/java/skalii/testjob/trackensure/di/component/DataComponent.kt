package skalii.testjob.trackensure.di.component


import dagger.Component

import javax.inject.Singleton

//import skalii.testjob.trackensure.data.local.TrackDatabase
import skalii.testjob.trackensure.di.module.LocalStorageModule


@Component(modules = [/*LocalStorageModule::class*//*, RemoteStorageModule::class*/])
@Singleton
interface DataComponent {

//    fun getDatabase(): TrackDatabase
//    fun getRestAPI(): RemoteApi

}