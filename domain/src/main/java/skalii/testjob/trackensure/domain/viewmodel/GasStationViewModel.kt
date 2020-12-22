package skalii.testjob.trackensure.domain.viewmodel


import android.content.Context

import skalii.testjob.trackensure.domain.repository.GasStationRepository
import skalii.testjob.trackensure.domain.viewmodel.base.impl.BaseViewModel
import skalii.testjob.trackensure.helper.model.GasStation


class GasStationViewModel : BaseViewModel<GasStation>() {

    override lateinit var repository: GasStationRepository


    fun init(context: Context) {
        repository = GasStationRepository(context)
    }


    fun getFinalLocal(title: String) =
        repository.loadSingleFinalLocal(title)


    override var getId: (GasStation) -> Int = { getFinalLocal(it.title)?.id ?: super.getId(it) }

}