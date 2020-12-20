package skalii.testjob.trackensure.domain.repository


import android.content.Context

import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository


@Suppress("EXPERIMENTAL_API_USAGE")
class SupplierRepository(context: Context) : BaseRepository<Supplier>(context) {

    override val dao = trackDatabase.getSupplierDao()


    fun loadSomeFinalLocal(name: String) =
        dao.findSomeFinal(name)

}