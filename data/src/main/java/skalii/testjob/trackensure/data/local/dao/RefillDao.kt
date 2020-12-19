package skalii.testjob.trackensure.data.local.dao


import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.data.local.dao.base.impl.BaseDao
import skalii.testjob.trackensure.helper.model.Refill


@Dao
@ExperimentalSerializationApi
abstract class RefillDao : BaseDao<Refill>() {

    override val orderBy: String = "order by date desc"


    @RawQuery(observedEntities = [Refill::class])
    abstract override fun findSingleLiveData(query: SupportSQLiteQuery): LiveData<Refill?>

    @RawQuery(observedEntities = [Refill::class])
    abstract override fun findSomeLiveData(query: SupportSQLiteQuery): LiveData<List<Refill>>


    @Query(
        """
            select *
            from refills
            where id != 0
            order by date desc
            """
    )
    abstract fun findAllPaging(): DataSource.Factory<Int, Refill>

}