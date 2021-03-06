package skalii.testjob.trackensure.helper.model


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentSnapshot

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
data class Supplier(

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @ColumnInfo(
        name = "name",
        collate = ColumnInfo.UNICODE,
        defaultValue = "Unknown supplier"
    )
    @NonNull
    var name: String = "Unknown supplier"

) : BaseModel, java.io.Serializable {

    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getDouble("id")?.toInt() ?: 0,
        documentSnapshot.getString("name") ?: "Unknown supplier"
    )


    override fun toString() =
        "Supplier(id=$id, name=$name)"

}