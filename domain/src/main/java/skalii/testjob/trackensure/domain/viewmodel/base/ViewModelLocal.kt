package skalii.testjob.trackensure.domain.viewmodel.base


import androidx.lifecycle.LiveData

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface ViewModelLocal<Model : BaseModel> {

    fun getModelClass(): Class<*>
    fun getModelName(): String

    fun checkExists(field: String, value: String): Boolean

    fun getLocal(id: Int?): LiveData<Model?>
    fun getFinalLocal(id: Int?): Model?

    fun getLocal(all: Boolean = false, vararg ids: Int? = emptyArray()): LiveData<List<Model>>
    fun getFinalLocal(all: Boolean = false, vararg ids: Int? = emptyArray()): List<Model>

    fun saveLocal(data: Model?): Long
    fun saveLocal(vararg data: Model?): List<Long>

    fun removeLocal(vararg ids: Int?): Int
    fun removeLocal(vararg data: Model?): Int

}