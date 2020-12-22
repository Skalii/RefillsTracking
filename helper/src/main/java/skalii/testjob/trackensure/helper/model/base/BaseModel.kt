package skalii.testjob.trackensure.helper.model.base


import com.google.firebase.firestore.DocumentSnapshot

import java.io.Serializable


interface BaseModel : Serializable {
    var id: Int

    companion object {

        fun <Model : BaseModel> DocumentSnapshot.toModel(clazz: Class<Model>): Model =
            clazz.getConstructor(DocumentSnapshot::class.java).newInstance(this)

        fun <Model : BaseModel> List<DocumentSnapshot>.toModels(clazz: Class<Model>): List<Model> =
            map { it.toModel(clazz) }

        fun <Model : BaseModel> List<Model>.findById(id: Int?) = find { it.id == id }
        fun <Model : BaseModel> List<Model>.setData(data: List<Model>) {
            if (this is MutableList<Model>) {
                clear(); addAll(data)
            } else {
                toMutableList().apply { clear(); addAll(data) }
            }
        }
    }

}