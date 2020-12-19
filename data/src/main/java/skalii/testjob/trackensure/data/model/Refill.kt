package skalii.testjob.trackensure.data.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import java.time.LocalDateTime

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import skalii.testjob.trackensure.data.local.converter.LocalDateSerializer
import skalii.testjob.trackensure.data.model.base.BaseModel
import skalii.testjob.trackensure.data.type.FuelType


@Entity(
    tableName = "refills",
    indices = [Index(
        value = ["id"],
        name = "refills_pkey",
        unique = true
    )],
    foreignKeys = [
        ForeignKey(
            entity = GasStation::class,
            parentColumns = ["id"],
            childColumns = ["id_gas_station"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Supplier::class,
            parentColumns = ["id"],
            childColumns = ["id_supplier"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@ExperimentalSerializationApi
@Serializable
data class Refill(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerialName(value = "id")
    override val id: Int,

    @ColumnInfo(
        name = "date",
        defaultValue = "(datetime('now'))"
    )
    @NonNull
    @SerialName(value = "date")
    @Serializable(with = LocalDateSerializer::class)
    var date: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "liter")
    @NonNull
    @SerialName(value = "liter")
    var liter: Double = 0.00,

    @ColumnInfo(
        name = "fuel_type",
        collate = ColumnInfo.UNICODE
    )
    @NonNull
    @SerialName(value = "fuel_type")
    var fuelType: FuelType,

    @ColumnInfo(name = "id_gas_station")
    @NonNull
    @SerialName(value = "id_gas_station")
//    @Transient
    var idGasStation: Int,

    @ColumnInfo(name = "id_supplier")
    @NonNull
    @SerialName(value = "id_supplier")
//    @Transient
    var idSupplier: Int,

    @ColumnInfo(name = "uid")
    @NonNull
    @SerialName(value = "uid")
//    @Transient
    var uid: String = "a0WgcHUYP7gRHQapRT8st3R5Cde2"

) : BaseModel