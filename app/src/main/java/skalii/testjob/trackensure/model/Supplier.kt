package skalii.testjob.trackensure.model


import skalii.testjob.trackensure.model.base.BaseModel


data class Supplier(
    override val id: Int,
    var name: String = "Unknown supplier"
) : BaseModel