package skalii.testjob.trackensure.ui.fragment


import android.os.Bundle
import android.view.View

import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView

import by.kirich1409.viewbindingdelegate.viewBinding

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.PageRefillBinding
import skalii.testjob.trackensure.model.Refill
import skalii.testjob.trackensure.model.base.BaseModel
import skalii.testjob.trackensure.setVerticalDivider
import skalii.testjob.trackensure.setVisibility
import skalii.testjob.trackensure.ui.adapter.base.BasePagedAdapter
import skalii.testjob.trackensure.ui.fragment.base.BasePageFragment


class PageRefillFragment : BasePageFragment(R.layout.page_refill) {

    override val viewBinding by viewBinding(PageRefillBinding::bind)

    override val dataLoadingProgressTags = mutableMapOf<String, Boolean>()

    private lateinit var refills: PagedList<Refill>


    override fun <T, Model : BaseModel> createRecycler(
        recyclerView: RecyclerView,
        adapter: BasePagedAdapter<Model>,
        func: () -> T?
    ): RecyclerView {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
//        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.setVerticalDivider()
        return recyclerView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.recyclerRefillsPageRefill.setVisibility(false)

    }

}