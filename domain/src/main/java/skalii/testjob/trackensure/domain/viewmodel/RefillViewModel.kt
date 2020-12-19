package skalii.testjob.trackensure.domain.viewmodel


import android.content.Context
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.domain.repository.RefillRepository
import skalii.testjob.trackensure.domain.viewmodel.base.impl.BaseViewModel


@Suppress("EXPERIMENTAL_API_USAGE")
class RefillViewModel : BaseViewModel<Refill>() {

    override lateinit var repository: RefillRepository

    fun init(context: Context) {
        repository = RefillRepository(context)
    }


    private val config =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
//            .setInitialLoadSizeHint(3)
//            .setMaxSize(9)
            .setPrefetchDistance(1)
            .setPageSize(10)
            .build()

    fun getAllPagingLocal() =
        LivePagedListBuilder(repository.loadAllPagingLocal(), config).build()

}