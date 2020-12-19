package skalii.testjob.trackensure.model


import skalii.testjob.trackensure.model.base.BaseModel


data class GasStation(
    override val id: Int,
    var title: String = "Unknown gas station",
    var geopoint: Pair<Double, Double> = Pair(0.00, 0.00)
) : BaseModel