package skalii.testjob.trackensure.domain.repository


import android.content.Context

import skalii.testjob.trackensure.data.model.GasStation
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository


@Suppress("EXPERIMENTAL_API_USAGE")
class GasStationRepository(context: Context) : BaseRepository<GasStation>(context) {

    override val dao = trackDatabase.getGasStationDao()

}