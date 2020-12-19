package skalii.testjob.trackensure.domain.repository


import android.content.Context

import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository


@Suppress("EXPERIMENTAL_API_USAGE")
class RefillRepository(context: Context) : BaseRepository<Refill>(context) {

    override val dao = trackDatabase.getRefillDao()

    fun loadAllPagingLocal() = dao.findAllPaging()

}