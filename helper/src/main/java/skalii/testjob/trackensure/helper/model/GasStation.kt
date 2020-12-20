package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import skalii.testjob.trackensure.helper.model.base.BaseModel


@Entity(
    tableName = "gas_stations",
    indices = [Index(
        value = ["id"],
        name = "gas_stations_pkey",
        unique = true
    ), Index(
        value = ["title"],
        name = "gas_stations_title_uindex",
        unique = true
    )]
)
@Serializable
data class GasStation(

    @ColumnInfo(
        name = "title",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown gas station"
    )
    @NonNull
    @SerialName(value = "title")
    var title: String = "Unknown gas station",

    @ColumnInfo(
        name = "geolocation", //todo geopoint
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown geolocation",
    )
    @NonNull
    @SerialName(value = "geopoint")
    var geopoint: Pair<Double, Double> = Pair(0.00, 0.00)

) : BaseModel {

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerialName(value = "id")
    override var id = 0


    constructor(
        id: Int,
        title: String = "Unknown gas station",
        geopoint: Pair<Double, Double> = Pair(0.00, 0.00)
    ) : this(title, geopoint) {
        this.id = id
    }


    override fun toString() =
        "GasStation(id=$id, title=$title, geopoint=$geopoint)"

}