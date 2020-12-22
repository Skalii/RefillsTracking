package skalii.testjob.trackensure.data.remote


import java.time.ZoneOffset
import java.util.Date

import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.data.remote.base.impl.BaseCollection
import skalii.testjob.trackensure.helper.model.Refill


@ExperimentalSerializationApi
class RefillCollection : BaseCollection<Refill>() {

    override val collectionName = "refills"
    override fun getModelClass() = Refill::class.java
    override fun getModelHashMap(model: Refill): HashMap<String, Any> = hashMapOf(
        "id" to model.id,
        "date" to Date.from(model.date.toInstant(ZoneOffset.UTC)),
        "liter" to model.liter,
        "cost" to model.cost,
        "fuel_type" to model.fuelType.value,
        "id_gas_station" to model.idGasStation,
        "id_supplier" to model.idSupplier,
        "uid" to model.uid
    )

}