package skalii.testjob.trackensure.domain.repository.base.impl


import android.content.Context

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import skalii.testjob.trackensure.data.local.TrackDatabase
import skalii.testjob.trackensure.data.local.dao.base.Dao
import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.domain.repository.base.RepositoryLocal
import skalii.testjob.trackensure.domain.repository.base.RepositoryRemote


abstract class BaseRepository<Model : BaseModel>(context: Context) :
    RepositoryLocal<Model>, RepositoryRemote<Model> {

    @Suppress("EXPERIMENTAL_API_USAGE")
    protected val trackDatabase = TrackDatabase.getInstance(context)
    protected abstract val dao: Dao<Model>
    protected val executor: ExecutorService = Executors.newSingleThreadExecutor()


    val loadAnyDataFromLocal: (() -> LiveData<*>) -> LiveData<*> =
        { Transformations.distinctUntilChanged(it()) }
    val saveAnyDataToLocal: (() -> Any) -> Any = { executor.submit(it).get() }
    val deleteAnyDataFromLocal: (deleteLocal: () -> Int) -> Int = { executor.submit(it).get() }

    override fun getModelClass() = dao.getModelClass()
    override fun getModelName() = dao.getModelName()
    override fun getTableName() = dao.getTableName()


    @Suppress("UNCHECKED_CAST")
    override fun loadSingleLocal(id: Int) =
        loadAnyDataFromLocal { dao.findSingle(id) } as LiveData<Model?>

    override fun loadSingleFinalLocal(id: Int) =
        dao.findSingleFinal(id)

    @Suppress("UNCHECKED_CAST")
    override fun loadFewLocal(ids: List<Int>) =
        loadAnyDataFromLocal { dao.findFew(ids) } as LiveData<List<Model>>

    override fun loadFewFinalLocal(ids: List<Int>) =
        dao.findFewFinal(ids)

    @Suppress("UNCHECKED_CAST")
    override fun loadAllLocal() =
        loadAnyDataFromLocal { dao.findAll() } as LiveData<List<Model>>

    override fun loadAllFinalLocal() =
        dao.findAllFinal()


    override fun saveLocal(record: Model) = saveAnyDataToLocal { dao.save(record) } as Long

    @Suppress("UNCHECKED_CAST")
    override fun saveLocal(records: List<Model>) =
        saveAnyDataToLocal { dao.save(records) } as List<Long>


    override fun deleteLocal(id: Int) = deleteAnyDataFromLocal { dao.delete(id) }
    override fun deleteFewLocal(ids: List<Int>) = deleteAnyDataFromLocal { dao.deleteFew(ids) }

    override fun deleteLocal(record: Model) = deleteAnyDataFromLocal { dao.delete(record) }
    override fun deleteAllLocal(records: List<Model>) =
        deleteAnyDataFromLocal { dao.deleteAll(records) }


    override fun runSync(record: Model?) {
        TODO("Not yet implemented")
    }

    override fun runSync(records: List<Model>?) {
        TODO("Not yet implemented")
    }

}