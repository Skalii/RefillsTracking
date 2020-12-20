package skalii.testjob.trackensure.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
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


    @Query(
        """
           select *
           from gas_stations
           where title = :title
             and geolocation = :geopoint
           order by title, geolocation;
           """
    )
    abstract fun findSomeFinal(title: String, geopoint: Pair<Double, Double>): List<GasStation>

}