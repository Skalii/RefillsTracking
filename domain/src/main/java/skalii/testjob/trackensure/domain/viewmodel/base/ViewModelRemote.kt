package skalii.testjob.trackensure.domain.viewmodel.base


import skalii.testjob.trackensure.helper.model.base.BaseModel


interface ViewModelRemote<Model : BaseModel> {

    fun getRemote(
        field: String,
        value: Any?,
        runOnSuccess: (found: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

    fun getRemote(
        runOnSuccess: (found: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )


    fun saveRemote(
        model: Model,
        runOnSuccess: (saved: Model) -> Unit,
        runOnFailure: () -> Unit
    )

    fun removeRemote(
        id: Int,
        runOnSuccess: (removed: List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

}