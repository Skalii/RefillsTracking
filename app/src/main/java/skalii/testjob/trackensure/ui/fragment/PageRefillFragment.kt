package skalii.testjob.trackensure.ui.fragment


import android.os.Bundle
import android.view.View

import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView

import by.kirich1409.viewbindingdelegate.viewBinding

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.PageRefillBinding
import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.domain.viewmodel.SupplierViewModel
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.setVerticalDivider
import skalii.testjob.trackensure.helper.setVisibility
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.ui.adapter.RefillPagedAdapter
import skalii.testjob.trackensure.ui.adapter.base.BasePagedAdapter
import skalii.testjob.trackensure.ui.fragment.base.BasePageFragment


class PageRefillFragment : BasePageFragment(R.layout.page_refill) {

    override val viewBinding by viewBinding(PageRefillBinding::bind)
    override val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    private val refillViewModel by viewModels<RefillViewModel>()
    private val gasStationViewModel by viewModels<GasStationViewModel>()
    private val supplierViewModel by viewModels<SupplierViewModel>()

    private val refillsLiveData = MutableLiveData<PagedList<Refill>>()
    private val gasStationLiveData = MutableLiveData<List<GasStation>>()
    private val suppliersLiveData = MutableLiveData<List<Supplier>>()

    override val dataLoadingProgressTags = mutableMapOf(
        "refills" to false, "gas_stations" to false, "suppliers" to false
    )

    private lateinit var refills: PagedList<Refill>


    override fun <T, Model : BaseModel> createRecycler(
        recyclerView: RecyclerView,
        adapter: BasePagedAdapter<Model>,
        func: () -> T?
    ): RecyclerView {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
//        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.setVerticalDivider(R.drawable.sh_divider_accent)
        return recyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refillViewModel.init(requireContext())
        gasStationViewModel.init(requireContext())
        supplierViewModel.init(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerRefills = viewBinding.recyclerRefillsPageRefill

        recyclerRefills.setVisibility(false)

        var isNotEmptyRefills = false
        val refillAdapter = RefillPagedAdapter()
        createRecycler(recyclerRefills, refillAdapter) {}


        refillViewModel.getAllPagingLocal()
            .observe(viewLifecycleOwner, setObserverPaging(refillsLiveData))

        refillsLiveData.observe(viewLifecycleOwner, { refills ->

            this.refills = refills
            isNotEmptyRefills = refills.isNotEmpty()

            gasStationViewModel
                .getLocal(ids = refills.map { it.idGasStation }.toTypedArray())
                .observe(viewLifecycleOwner, setObserverSome(gasStationLiveData))

            supplierViewModel
                .getLocal(ids = refills.map { it.idSupplier }.toTypedArray())
                .observe(viewLifecycleOwner, setObserverSome(suppliersLiveData))

            dataProgressionLiveData
                .postValue(dataLoadingProgressTags.apply { this["refills"] = true })
        })

        gasStationLiveData.observe(viewLifecycleOwner, { gasStations ->

            refillAdapter.setDataGasStations(gasStations)

            dataProgressionLiveData
                .postValue(dataLoadingProgressTags.apply { this["gas_stations"] = true })
        })

        suppliersLiveData.observe(viewLifecycleOwner, { suppliers ->

            refillAdapter.setDataSuppliers(suppliers)

            dataProgressionLiveData
                .postValue(dataLoadingProgressTags.apply { this["suppliers"] = true })
        })


        dataProgressionLiveData.observe(viewLifecycleOwner, { list ->

            list.forEach {
                logNav("dataProgressionLiveData(\"${it.key}\") = ${it.value}")
            }

            if (!list.containsValue(false)) {

                viewBinding.progressInitPageRefill.setVisibility(false)
                recyclerRefills.setVisibility(isNotEmptyRefills)
                if (isNotEmptyRefills) refillAdapter.submitList(refills)
                else refillAdapter.clearData()

            }
        })

    }


    override fun onDestroyView() {

        refills.dataSource.invalidate()

        refillsLiveData.removeObservers(viewLifecycleOwner)
        gasStationLiveData.removeObservers(viewLifecycleOwner)
        suppliersLiveData.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }

}