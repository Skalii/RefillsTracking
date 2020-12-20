package skalii.testjob.trackensure.domain.viewmodel.base.impl


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

}