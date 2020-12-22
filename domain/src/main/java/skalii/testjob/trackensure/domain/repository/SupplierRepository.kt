package skalii.testjob.trackensure.domain.repository


import android.content.Context

import skalii.testjob.trackensure.data.remote.SupplierCollection
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository


class SupplierRepository(context: Context) : BaseRepository<Supplier>(context) {

    override val dao = localDatabase.getSupplierDao()
    override val remoteDatabase = SupplierCollection()


    fun loadSingleFinalLocal(name: String) =
        dao.findSingleFinal(name)

}