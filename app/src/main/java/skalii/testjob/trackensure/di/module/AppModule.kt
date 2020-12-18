package skalii.testjob.trackensure.di.module


import android.content.Context

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

import skalii.testjob.trackensure.TrackApplication


@Module
class AppModule(private val application: TrackApplication) {

    @[Provides Singleton]
    fun provideApplicationContext(): Context = application.applicationContext

}