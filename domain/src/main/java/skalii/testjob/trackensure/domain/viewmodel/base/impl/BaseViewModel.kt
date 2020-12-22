package skalii.testjob.trackensure.domain.viewmodel.base.impl


import android.util.Log

import androidx.lifecycle.ViewModel

import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.domain.repository.base.impl.BaseRepository
import skalii.testjob.trackensure.domain.viewmodel.base.ViewModelLocal
import skalii.testjob.trackensure.domain.viewmodel.base.ViewModelRemote


abstract class BaseViewModel<Model : BaseModel> :
    ViewModelLocal<Model>, ViewModelRemote<Model>, ViewModel() {

    protected abstract val repository: BaseRepository<Model>


    override fun getModelClass() = repository.getModelClass()
    override fun getModelName() = repository.getModelName()


    override fun checkExists(field: String, value: String) =
        repository.checkExists(field, value)

    override fun getLocal(id: Int?) =
        repository.loadSingleLocal(id ?: -1)

    override fun getFinalLocal(id: Int?) =
        if (id != null) repository.loadSingleFinalLocal(id) else null


    override fun getLocal(all: Boolean, vararg ids: Int?) =
        if (all) repository.loadAllLocal()
        else repository.loadFewLocal(ids.filterNotNull())

    override fun getFinalLocal(all: Boolean, vararg ids: Int?) =
        when {
            all -> repository.loadAllFinalLocal()
            ids.isNotEmpty() -> repository.loadFewFinalLocal(ids.filterNotNull())
            else -> emptyList()
        }


    override fun saveLocal(data: Model?) =
        if (data != null) repository.saveLocal(data) else 0L

    override fun saveLocal(vararg data: Model?) =
        if (!data.isNullOrEmpty()) repository.saveLocal(data.filterNotNull())
        else emptyList()


    override fun removeLocal(vararg ids: Int?) =
        if (!ids.isNullOrEmpty()) repository.deleteFewLocal(ids.filterNotNull()) else 0

    override fun removeLocal(vararg data: Model?) =
        if (!data.isNullOrEmpty()) repository.deleteAllLocal(data.filterNotNull()) else 0


    override fun getRemote(
        field: String,
        value: Any?,
        runOnSuccess: (found: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) =
        repository.loadRemote(field, value, runOnSuccess, runOnFailure)

    override fun getRemote(
        runOnSuccess: (found: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) =
        repository.loadRemote(runOnSuccess, runOnFailure)


    override fun saveRemote(
        model: Model,
        runOnSuccess: (saved: Model) -> Unit,
        runOnFailure: () -> Unit,
        isNew: Boolean
    ) =
        repository.saveRemote(model, runOnSuccess, runOnFailure, isNew)

    override fun removeRemote(
        id: Int,
        runOnSuccess: (removed: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) =
        repository.deleteRemote(id, runOnSuccess, runOnFailure)


    override fun runSync(
        model: Model,
        runOnSuccess: (id: Int) -> Unit,
        runOnFailure: () -> Unit,
        isNew: Boolean
    ) {
        var id = saveLocal(model).toInt()
        if (id == -1) id = getId(model)

        val local = getFinalLocal(id)
        Log.d("NEW_MODEL_LOCAL", local.toString())
        saveRemote(local!!, {
            Log.d("NEW_MODEL_REMOTE", it.toString())
            runOnSuccess(it.id)
        }, { runOnFailure() }, isNew)
    }

    protected open var getId: (Model) -> Int = { it.id }

}