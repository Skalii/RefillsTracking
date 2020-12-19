package skalii.testjob.trackensure.model


import java.time.LocalDateTime

import skalii.testjob.trackensure.model.base.BaseModel


data class Refill(
    override val id: Int,
    var date: LocalDateTime = LocalDateTime.now(),
    var liter: Double = 0.00,
    var fuelType: String, //todo FuelType
    var idGasStation: Int,
    var idSupplier: Int,
    var uid: String = "a0WgcHUYP7gRHQapRT8st3R5Cde2"
) : BaseModel