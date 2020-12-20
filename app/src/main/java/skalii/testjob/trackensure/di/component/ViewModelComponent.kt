package skalii.testjob.trackensure.di.component


import dagger.Component

import skalii.testjob.trackensure.di.module.ViewModelModule
import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.service.ModelsSaverService


@Component(modules = [ViewModelModule::class])
interface ViewModelComponent {

    fun getRefillViewModel(): RefillViewModel
    fun getGasStationViewModel(): GasStationViewModel
    fun getSupplierViewModel(): SupplierViewModel

    fun injectService(service: ModelsSaverService)

}