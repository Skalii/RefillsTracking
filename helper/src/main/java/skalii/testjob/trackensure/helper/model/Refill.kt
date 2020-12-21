package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentSnapshot

import java.time.LocalDateTime

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import skalii.testjob.trackensure.helper.converter.LocalDateSerializer
import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.helper.type.FuelType
import java.time.ZoneOffset


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
    override var id: Int = 0,

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

    @ColumnInfo(name = "cost")
    @NonNull
    @SerialName(value = "cost")
    var cost: Double = 0.00,

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
    var idGasStation: Int = 0,

    @ColumnInfo(name = "id_supplier")
    @NonNull
    @SerialName(value = "id_supplier")
//    @Transient
    var idSupplier: Int = 0,

    @ColumnInfo(name = "uid")
    @NonNull
    @SerialName(value = "uid")
//    @Transient
    var uid: String = "a0WgcHUYP7gRHQapRT8st3R5Cde2"

) : BaseModel {

    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getDouble("id")?.toInt() ?: 0,
        LocalDateTime.ofInstant(
            documentSnapshot.getDate("date")!!.toInstant(),
            ZoneOffset.systemDefault()
        ),
        documentSnapshot.getDouble("liter") ?: 0.00,
        documentSnapshot.getDouble("double") ?: 0.00,
        FuelType.toEnum(documentSnapshot.getString("fuel_type") ?: ""),
        documentSnapshot.getDouble("id_gas_station")?.toInt() ?: 0,
        documentSnapshot.getDouble("id_supplier")?.toInt() ?: 0,
        documentSnapshot.getString("uid") ?: "a0WgcHUYP7gRHQapRT8st3R5Cde2"
    )


    override fun toString() =
        "Refill(id=$id, date=$date, liter=$liter, cost=$cost, fuelType=$fuelType, idGasStation=$idGasStation, idSupplier=$idSupplier, uid=$uid)"

}