package skalii.testjob.trackensure.domain.repository.base


import androidx.lifecycle.LiveData

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface RepositoryLocal<Model : BaseModel> {

    fun getModelClass(): Class<*>
    fun getModelName(): String
    fun getTableName(): String

    fun loadSingleLocal(id: Int): LiveData<Model?>
    fun loadSingleFinalLocal(id: Int): Model?
    fun loadFewLocal(ids: List<Int>): LiveData<List<Model>>
    fun loadFewFinalLocal(ids: List<Int>): List<Model>
    fun loadAllLocal(): LiveData<List<Model>>
    fun loadAllFinalLocal(): List<Model>

    fun saveLocal(record: Model)
    fun saveLocal(records: List<Model>)

    fun deleteLocal(id: Int)
    fun deleteFewLocal(ids: List<Int>)
    fun deleteLocal(record: Model)
    fun deleteAllLocal(records: List<Model>)

}