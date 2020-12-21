package skalii.testjob.trackensure.data.remote.base.impl


import android.util.Log

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot

import skalii.testjob.trackensure.data.remote.base.Collection
import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.helper.model.base.BaseModel.Companion.toModel
import skalii.testjob.trackensure.helper.model.base.BaseModel.Companion.toModels


abstract class BaseCollection<Model : BaseModel> : Collection<Model> {

    protected abstract val collectionName: String
    protected abstract fun getModelClass(): Class<Model>
    protected fun getCollection() = firestore.collection(collectionName)
    protected val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }


    protected fun onSuccessListenerSingle(runOnSuccess: (Model) -> Unit) =
        OnSuccessListener<DocumentSnapshot> { document ->
            if (document != null) {
                Log.e("COLLECTION", "Such document")
                runOnSuccess(document.toModel(getModelClass()))
            } else Log.w("COLLECTION", "No such document")
        }

    protected fun onSuccessListenerSome(runOnSuccess: (List<Model>) -> Unit) =
        OnSuccessListener<QuerySnapshot> { document ->
            if (document != null) {
                Log.e("COLLECTION", "Such document")
                runOnSuccess(document.documents.toModels(getModelClass()))
            } else Log.w("COLLECTION", "No such document")
        }

    protected fun onFailureListener(runOnFailure: () -> Unit) =
        OnFailureListener { exception ->
            Log.w("COLLECTION", "Get failed with ", exception); runOnFailure()
        }

    override fun find(
        field: String,
        value: Any?,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) {
        getCollection().whereEqualTo(field, value).get()
            .addOnSuccessListener(onSuccessListenerSome { runOnSuccess(it) })
            .addOnFailureListener(onFailureListener { runOnFailure() })
    }

    override fun findAll(
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) {
        getCollection().get()
            .addOnSuccessListener(onSuccessListenerSome { runOnSuccess(it) })
            .addOnFailureListener(onFailureListener { runOnFailure() })
    }


    override fun delete(
        id: Int,
        runOnSuccess: (List<Model>) -> Unit,
        runOnFailure: () -> Unit
    ) {
        getCollection().whereEqualTo("id", id).get()
            .addOnSuccessListener {
                if (it != null) {
                    Log.e("COLLECTION", "Such document")
                    runOnSuccess(it.documents.toModels(getModelClass()))
                    it.documents[0].reference.delete()
                } else Log.w("COLLECTION", "No such document")
            }
            .addOnFailureListener {
                Log.w("COLLECTION", "Get failed with ", it); runOnFailure()
            }
    }

}