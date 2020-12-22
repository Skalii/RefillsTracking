package skalii.testjob.trackensure.data.local.dao.base.impl


import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

import java.lang.reflect.ParameterizedType
import java.util.Locale.ROOT

import skalii.testjob.trackensure.data.local.dao.base.Dao
import skalii.testjob.trackensure.helper.model.base.BaseModel


abstract class BaseDao<Model : BaseModel> : Dao<Model> {

    override fun getModelClass() =
        (javaClass.superclass!!.genericSuperclass as ParameterizedType?)
            ?.actualTypeArguments?.get(0) as Class<*>

    override fun getModelName(): String = getModelClass().simpleName
    override fun getTableName() = getModelName().toLowerCase(ROOT).plus("s")

    protected abstract val orderBy: String


    @JvmSynthetic
    protected abstract fun findSingleLiveData(query: SupportSQLiteQuery): LiveData<Model?>

    @RawQuery
    protected abstract fun findSingleNotLiveData(query: SupportSQLiteQuery): Model?

    @JvmSynthetic
    protected abstract fun findSomeLiveData(query: SupportSQLiteQuery): LiveData<List<Model>>

    @RawQuery
    protected abstract fun findSomeNotLiveData(query: SupportSQLiteQuery): List<Model>

    @RawQuery
    protected abstract fun checkSingleNotLiveData(query: SupportSQLiteQuery): Boolean

    @RawQuery
    protected abstract fun executeQuery(query: SupportSQLiteQuery): Int


    override fun checkExists(field: String, value: String) =
        checkSingleNotLiveData(
            SimpleSQLiteQuery("select exists(select * from ${getTableName()} where $field = \"$value\" and id != 0);")
        )

    override fun findSingle(id: Int) =
        findSingleLiveData(
            SimpleSQLiteQuery("select * from ${getTableName()} where id = $id and id != 0;")
        )

    override fun findSingleFinal(id: Int) =
        findSingleNotLiveData(
            SimpleSQLiteQuery("select * from ${getTableName()} where id = $id and id != 0;")
        )

    override fun findFew(ids: List<Int>) =
        findSomeLiveData(
            SimpleSQLiteQuery("select * from ${getTableName()} where id in (${ids.joinToString()}) and id != 0 $orderBy;")
        )

    override fun findFewFinal(ids: List<Int>) =
        findSomeNotLiveData(
            SimpleSQLiteQuery(
                "select * from ${getTableName()} where id in (${ids.joinToString()}) and id != 0 $orderBy;"
            )
        )

    override fun findAll() =
        findSomeLiveData(
            SimpleSQLiteQuery("select * from ${getTableName()} where id != 0 $orderBy;")
        )

    override fun findAllFinal() =
        findSomeNotLiveData(
            SimpleSQLiteQuery("select * from ${getTableName()} where id != 0 $orderBy;")
        )


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insert(record: Model): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insert(records: List<Model>): List<Long>

    @Update
    protected abstract fun update(record: Model)

    @Update
    protected abstract fun update(records: List<Model>)

    @Transaction
    override fun save(record: Model) =
        insert(record).also { if (it == -1L) update(record) }

    @Transaction
    override fun save(records: List<Model>) =
        insert(records).filterNot { it == -1L }.also { insertIds ->
            update(records.filterNot { fn -> fn.id.toLong() in insertIds })
        }


    override fun delete(id: Int) =
        executeQuery(
            SimpleSQLiteQuery("delete from ${getTableName()} where id = $id;")
        )

    override fun deleteFew(ids: List<Int>) =
        executeQuery(
            SimpleSQLiteQuery("delete from ${getTableName()} where id in (${ids.joinToString()});")
        )

    @Delete
    abstract override fun delete(record: Model): Int

    @Delete
    abstract override fun deleteAll(records: List<Model>): Int

}