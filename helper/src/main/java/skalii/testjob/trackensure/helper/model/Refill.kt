package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentSnapshot

import java.time.LocalDateTime
import java.time.ZoneOffset

import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.helper.type.FuelType


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
data class Refill(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @ColumnInfo(
        name = "date",
        defaultValue = "(datetime('now'))"
    )
    @NonNull
    var date: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "liter")
    @NonNull
    var liter: Double = 0.00,

    @ColumnInfo(name = "cost")
    @NonNull
    var cost: Double = 0.00,

    @ColumnInfo(
        name = "fuel_type",
        collate = ColumnInfo.UNICODE
    )
    @NonNull
    var fuelType: FuelType = FuelType.UNKNOWN,

    @ColumnInfo(name = "id_gas_station")
    @NonNull
    var idGasStation: Int = 0,

    @ColumnInfo(name = "id_supplier")
    @NonNull
    var idSupplier: Int = 0,

    @ColumnInfo(name = "uid")
    @NonNull
    var uid: String = "a0WgcHUYP7gRHQapRT8st3R5Cde2"

) : BaseModel {

    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getDouble("id")?.toInt() ?: 0,
        LocalDateTime.ofInstant(
            documentSnapshot.getDate("date")!!.toInstant(),
            ZoneOffset.systemDefault()
        ),
        documentSnapshot.getDouble("liter") ?: 0.00,
        documentSnapshot.getDouble("cost") ?: 0.00,
        FuelType.toEnum(documentSnapshot.getString("fuel_type") ?: ""),
        documentSnapshot.getDouble("id_gas_station")?.toInt() ?: 0,
        documentSnapshot.getDouble("id_supplier")?.toInt() ?: 0,
        documentSnapshot.getString("uid") ?: "a0WgcHUYP7gRHQapRT8st3R5Cde2"
    )


    override fun toString() =
        "Refill(id=$id, date=$date, liter=$liter, cost=$cost, fuelType=$fuelType, idGasStation=$idGasStation, idSupplier=$idSupplier, uid=$uid)"

}