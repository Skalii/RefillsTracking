package skalii.testjob.trackensure.domain.viewmodel


import android.content.Context

import skalii.testjob.trackensure.domain.repository.SupplierRepository
import skalii.testjob.trackensure.domain.viewmodel.base.impl.BaseViewModel
import skalii.testjob.trackensure.helper.model.Supplier


class SupplierViewModel : BaseViewModel<Supplier>() {

    override lateinit var repository: SupplierRepository


    fun init(context: Context) {
        repository = SupplierRepository(context)
    }


    fun getFinalLocal(title: String) =
        repository.loadSingleFinalLocal(title)


    override var getId: (Supplier) -> Int = { getFinalLocal(it.name)?.id ?: super.getId(it) }

}