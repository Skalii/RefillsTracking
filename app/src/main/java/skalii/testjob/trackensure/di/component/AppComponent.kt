package skalii.testjob.trackensure.di.component


import android.content.Context

import dagger.Component

import javax.inject.Singleton

import skalii.testjob.trackensure.di.module.AppModule


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun getContext(): Context

}