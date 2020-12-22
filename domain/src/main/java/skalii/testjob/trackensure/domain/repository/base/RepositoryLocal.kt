package skalii.testjob.trackensure.domain.repository.base


import androidx.lifecycle.LiveData

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface RepositoryLocal<Model : BaseModel> {

    fun getModelClass(): Class<*>
    fun getModelName(): String
    fun getTableName(): String

    fun checkExists(field: String, value: String): Boolean

    fun loadSingleLocal(id: Int): LiveData<Model?>
    fun loadSingleFinalLocal(id: Int): Model?
    fun loadFewLocal(ids: List<Int>): LiveData<List<Model>>
    fun loadFewFinalLocal(ids: List<Int>): List<Model>
    fun loadAllLocal(): LiveData<List<Model>>
    fun loadAllFinalLocal(): List<Model>

    fun saveLocal(record: Model): Long
    fun saveLocal(records: List<Model>): List<Long>

    fun deleteLocal(id: Int): Int
    fun deleteFewLocal(ids: List<Int>): Int
    fun deleteLocal(record: Model): Int
    fun deleteAllLocal(records: List<Model>): Int

}