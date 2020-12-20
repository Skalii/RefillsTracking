package skalii.testjob.trackensure.domain.repository.base


import skalii.testjob.trackensure.helper.model.base.BaseModel


interface RepositoryRemote<Model : BaseModel> {

    fun runSync(record: Model?)
    fun runSync(records: List<Model>?)

}