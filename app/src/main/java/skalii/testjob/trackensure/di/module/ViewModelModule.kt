package skalii.testjob.trackensure.di.module


import dagger.Module
import dagger.Provides

import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel


@Module
class ViewModelModule(
    private val refillViewModel: RefillViewModel,
    private val gasStationViewModel: GasStationViewModel,
    private val supplierViewModel: SupplierViewModel,
) {

    @Provides
    fun provideRefillViewModel(): RefillViewModel = refillViewModel

    @Provides
    fun provideGasStationViewModel(): GasStationViewModel = gasStationViewModel

    @Provides
    fun provideSupplierViewModel(): SupplierViewModel = supplierViewModel

}