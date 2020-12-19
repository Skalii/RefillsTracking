package skalii.testjob.trackensure.ui.adapter.base


import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.helper.model.base.BaseModel.Companion.setData


abstract class BasePagedAdapter<Model : BaseModel> :
    PagedListAdapter<Model, BaseViewHolder>(DiffUtilCallBack<Model>()) {

    abstract fun clearData()
    protected fun <ListModel : List<BaseModel>> setData(newData: ListModel, oldData: ListModel) {
        oldData.setData(newData)
        notifyDataSetChanged()
    }


    class DiffUtilCallBack<Model : BaseModel> : DiffUtil.ItemCallback<Model>() {
        override fun areItemsTheSame(oldItem: Model, newItem: Model) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Model, newItem: Model) = oldItem.equals(newItem)
    }

}