package skalii.testjob.trackensure.data.remote


import skalii.testjob.trackensure.data.remote.base.impl.BaseCollection
import skalii.testjob.trackensure.helper.model.Supplier


class SupplierCollection : BaseCollection<Supplier>() {

    override val collectionName = "suppliers"
    override fun getModelClass() = Supplier::class.java

    override fun add(
        model: Supplier,
        runOnSuccess: (Supplier) -> Unit,
        runOnFailure: () -> Unit
    ) {
        getCollection().add(
            hashMapOf(
                "id" to model.id,
                "name" to model.name
            )
        ).addOnSuccessListener { reference ->
            reference.get().addOnSuccessListener(onSuccessListenerSingle { runOnSuccess(it) })
        }.addOnFailureListener(onFailureListener { runOnFailure() })
    }

}