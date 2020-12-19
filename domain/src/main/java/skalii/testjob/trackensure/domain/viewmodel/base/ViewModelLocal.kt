package skalii.testjob.trackensure.domain.viewmodel.base


import androidx.lifecycle.LiveData

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface ViewModelLocal<Model : BaseModel> {

    fun getModelClass(): Class<*>
    fun getModelName(): String

    fun getLocal(id: Int?): LiveData<Model?>
    fun getFinalLocal(id: Int?): Model?

    fun getLocal(all: Boolean = false, vararg ids: Int? = emptyArray()): LiveData<List<Model>>
    fun getFinalLocal(all: Boolean = false, vararg ids: Int? = emptyArray()): List<Model>

    fun saveLocal(vararg data: Model?)

    fun removeLocal(vararg ids: Int?)
    fun removeLocal(vararg data: Model?)

}