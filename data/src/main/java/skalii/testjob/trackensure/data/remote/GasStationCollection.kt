package skalii.testjob.trackensure.data.remote


import com.google.firebase.firestore.GeoPoint

import skalii.testjob.trackensure.data.remote.base.impl.BaseCollection
import skalii.testjob.trackensure.helper.model.GasStation


class GasStationCollection : BaseCollection<GasStation>() {

    override val collectionName = "gas_stations"
    override fun getModelClass() = GasStation::class.java

    override fun add(
        model: GasStation,
        runOnSuccess: (GasStation) -> Unit,
        runOnFailure: () -> Unit
    ) {
        getCollection().add(
            hashMapOf(
                "id" to model.id,
                "title" to model.title,
                "geopoint" to GeoPoint(model.geopoint.first, model.geopoint.second)
            )
        ).addOnSuccessListener { reference ->
            reference.get().addOnSuccessListener(onSuccessListenerSingle { runOnSuccess(it) })
        }.addOnFailureListener(onFailureListener { runOnFailure() })
    }

}