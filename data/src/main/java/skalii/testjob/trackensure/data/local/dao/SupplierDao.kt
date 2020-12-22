package skalii.testjob.trackensure.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

import skalii.testjob.trackensure.data.local.dao.base.impl.BaseDao
import skalii.testjob.trackensure.helper.model.Supplier


@Dao
abstract class SupplierDao : BaseDao<Supplier>() {

    override val orderBy: String = "order by name"


    @RawQuery(observedEntities = [Supplier::class])
    abstract override fun findSingleLiveData(query: SupportSQLiteQuery): LiveData<Supplier?>

    @RawQuery(observedEntities = [Supplier::class])
    abstract override fun findSomeLiveData(query: SupportSQLiteQuery): LiveData<List<Supplier>>


    @Query(
        """
           select *
           from suppliers
           where name = :name
           order by name;
           """
    )
    abstract fun findSingleFinal(name: String): Supplier?

}