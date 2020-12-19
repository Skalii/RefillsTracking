package skalii.testjob.trackensure.ui.adapter.base


import androidx.recyclerview.widget.RecyclerView.Adapter

import skalii.testjob.trackensure.model.base.BaseModel
import skalii.testjob.trackensure.model.base.BaseModel.Companion.setData


abstract class BaseCommonAdapter : Adapter<BaseViewHolder>() {

    protected abstract val data: List<BaseModel>


    abstract fun clearData()
    protected fun <ListModel : List<BaseModel>> setData(newData: ListModel, oldData: ListModel) {
        oldData.setData(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

}