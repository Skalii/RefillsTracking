package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentSnapshot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import skalii.testjob.trackensure.helper.model.base.BaseModel


@Entity(
    tableName = "suppliers",
    indices = [
        Index(
            value = ["id"],
            name = "suppliers_pkey",
            unique = true
        ), Index(
            value = ["name"],
            name = "suppliers_name_uindex",
            unique = true
        )]
)
@Serializable
data class Supplier(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerialName(value = "id")
    override var id: Int = 0,

    @ColumnInfo(
        name = "name",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown supplier"
    )
    @NonNull
    @SerialName(value = "name")
    var name: String = "Unknown supplier"

) : BaseModel {

    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getDouble("id")?.toInt() ?: 0,
        documentSnapshot.getString("name") ?: "Unknown supplier"
    )


    override fun toString() =
        "Supplier(id=$id, name=$name)"

}