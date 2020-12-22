package skalii.testjob.trackensure.data.remote.base


import com.google.firebase.firestore.DocumentReference

import skalii.testjob.trackensure.helper.model.base.BaseModel


interface Collection<Model : BaseModel> {

    fun find(
        field: String,
        value: Any?,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

    fun findAll(
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

    fun findDocumentByModelId(
        id: Int?,
        runOnSuccess: (DocumentReference) -> Unit,
        runOnFailure: () -> Unit
    )

    fun add(
        model: Model,
        runOnSuccess: (Model) -> Unit,
        runOnFailure: () -> Unit
    )

    fun set(
        model: Model,
        runOnSuccess: (Model) -> Unit,
        runOnFailure: () -> Unit
    )

    fun delete(
        id: Int,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    )

}