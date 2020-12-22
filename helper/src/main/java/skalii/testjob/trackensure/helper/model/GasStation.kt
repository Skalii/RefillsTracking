package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentSnapshot

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
data class GasStation(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @ColumnInfo(
        name = "title",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown gas station"
    )
    @NonNull
    var title: String = "Unknown gas station",

    @ColumnInfo(
        name = "geolocation", //todo geopoint
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown geolocation",
    )
    @NonNull
    var geopoint: Pair<Double, Double> = Pair(0.00, 0.00)

) : BaseModel, java.io.Serializable {

    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getDouble("id")?.toInt() ?: 0,
        documentSnapshot.getString("title") ?: "Unknown gas station",
        Pair(
            documentSnapshot.getGeoPoint("geopoint")?.latitude ?: 0.00,
            documentSnapshot.getGeoPoint("geopoint")?.latitude ?: 0.00
        ),
    )


    override fun toString() =
        "GasStation(id=$id, title=$title, geopoint=$geopoint)"

}