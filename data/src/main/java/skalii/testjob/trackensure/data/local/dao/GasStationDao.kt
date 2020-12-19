package skalii.testjob.trackensure.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

import skalii.testjob.trackensure.data.local.dao.base.impl.BaseDao
import skalii.testjob.trackensure.helper.model.GasStation


@Dao
abstract class GasStationDao : BaseDao<GasStation>() {

    override val orderBy: String = "order by title, geolocation"
    override fun getTableName() = "gas_stations"


    @RawQuery(observedEntities = [GasStation::class])
    abstract override fun findSingleLiveData(query: SupportSQLiteQuery): LiveData<GasStation?>

    @RawQuery(observedEntities = [GasStation::class])
    abstract override fun findSomeLiveData(query: SupportSQLiteQuery): LiveData<List<GasStation>>

}