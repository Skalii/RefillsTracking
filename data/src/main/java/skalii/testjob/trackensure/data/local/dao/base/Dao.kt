package skalii.testjob.trackensure.data.local.dao.base


import androidx.lifecycle.LiveData

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface Dao<Model : BaseModel> {

    fun getModelClass(): Class<*>
    fun getModelName(): String
    fun getTableName(): String

    fun findSingle(id: Int): LiveData<Model?>
    fun findSingleFinal(id: Int): Model?
    fun findFew(ids: List<Int>): LiveData<List<Model>>
    fun findFewFinal(ids: List<Int>): List<Model>
    fun findAll(): LiveData<List<Model>>
    fun findAllFinal(): List<Model>

    fun save(record: Model)
    fun save(records: List<Model>)

    fun delete(id: Int): Int
    fun deleteFew(ids: List<Int>): Int
    fun delete(record: Model): Int
    fun deleteAll(records: List<Model>): Int

}