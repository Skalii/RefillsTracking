package skalii.testjob.trackensure.domain.repository


import android.content.Context

import skalii.testjob.trackensure.data.remote.GasStationCollection
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository


class GasStationRepository(context: Context) : BaseRepository<GasStation>(context) {

    override val dao = localDatabase.getGasStationDao()
    override val remoteDatabase = GasStationCollection()


    fun loadSingleFinalLocal(title: String) =
        dao.findSingleFinal(title)

}