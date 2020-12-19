package skalii.testjob.trackensure.ui.adapter.base


import android.view.View

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder(view: View) : ViewHolder(view) {
    protected abstract val viewBinding: ViewBinding
}