package skalii.testjob.trackensure.data.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import skalii.testjob.trackensure.data.model.base.BaseModel


@Entity(
    tableName = "gas_stations",
    indices = [Index(
        value = ["id"],
        name = "gas_stations_pkey",
        unique = true
    )]
)
@Serializable
data class GasStation(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerialName(value = "id")
    override val id: Int,

    @ColumnInfo(
        name = "title",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown gas station"
    )
    @NonNull
    @SerialName(value = "title")
    var title: String = "Unknown gas station",

    @ColumnInfo(
        name = "geolocation",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown geolocation",
    )
    @NonNull
    @SerialName(value = "geolocation")
    var geolocation: String = "Unknown geolocation"

) : BaseModel