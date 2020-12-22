package skalii.testjob.trackensure.domain.repository.base


import skalii.testjob.trackensure.helper.model.base.BaseModel


interface RepositoryRemote<Model : BaseModel> {

    fun loadRemote(
        field: String,
        value: Any?,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

    fun loadRemote(
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

    fun saveRemote(
        model: Model,
        runOnSuccess: (Model) -> Unit,
        runOnFailure: () -> Unit,
        isNew: Boolean = true
    )

    fun deleteRemote(
        id: Int,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

}