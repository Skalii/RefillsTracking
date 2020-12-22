package skalii.testjob.trackensure.ui.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow

import androidx.annotation.LayoutRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData

import by.kirich1409.viewbindingdelegate.viewBinding

import java.math.RoundingMode

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import kotlinx.serialization.ExperimentalSerializationApi

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.PageStatisticBinding
import skalii.testjob.trackensure.databinding.TableRowStatisticBinding
import skalii.testjob.trackensure.domain.viewmodel.GasStationViewModel
import skalii.testjob.trackensure.domain.viewmodel.RefillViewModel
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.base.BaseModel.Companion.setData
import skalii.testjob.trackensure.ui.fragment.base.BasePageFragment


@ExperimentalSerializationApi
class PageStatisticFragment : BasePageFragment(R.layout.page_statistic) {

    override val viewBinding by viewBinding(PageStatisticBinding::bind)
    override val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    private val refillViewModel by viewModels<RefillViewModel>()
    private val gasStationViewModel by viewModels<GasStationViewModel>()

    private val refillsLiveData = MutableLiveData<List<Refill>>()
    private val gasStationLiveData = MutableLiveData<List<GasStation>>()

    private val refillsData = mutableListOf<Refill/*?*/>()
    private val gasStationsData = mutableListOf<GasStation/*?*/>()

    override val dataLoadingProgressTags = mutableMapOf(
        "refills" to false, "gas_stations" to false
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refillViewModel.init(requireContext())
        gasStationViewModel.init(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isNotEmptyRefills = false

        refillViewModel.getLocal(true)
            .observe(viewLifecycleOwner, setObserverSome(refillsLiveData))


        refillsLiveData.observe(viewLifecycleOwner, { refills ->

            isNotEmptyRefills = refills.isNotEmpty()

            /*refillsData.replaceAll { refillOld ->
                refills.find { refillNew -> refillNew.id == refillOld?.id }
            }
            refillsData.addAll(
                refills.filterNot { refillNew ->
                    refillNew.id == refillsData.find { it?.id == refillNew.id }?.id
                }
            )*/

            refillsData.setData(refills)

            gasStationViewModel
                .getLocal(ids = refills.map { it.idGasStation }.toTypedArray())
                .observe(viewLifecycleOwner, setObserverSome(gasStationLiveData))


            dataProgressionLiveData
                .postValue(dataLoadingProgressTags.apply { this["refills"] = true })
        })

        gasStationLiveData.observe(viewLifecycleOwner, { gasStations ->

            /*gasStationsData.replaceAll { gasStationOld ->
                gasStations.find { gasStationNew -> gasStationNew.id == gasStationOld?.id }
            }
            gasStationsData.addAll(
                gasStations.filterNot { gasStationNew ->
                    gasStationNew.id == gasStationsData.find { it?.id == gasStationNew.id }?.id
                }
            )*/

            gasStationsData.setData(gasStations)

            dataProgressionLiveData
                .postValue(dataLoadingProgressTags.apply { this["gas_stations"] = true })
        })

        dataProgressionLiveData.observe(viewLifecycleOwner, { list ->

            list.forEach {
                logNav("dataProgressionLiveData(\"${it.key}\") = ${it.value}")
            }

            if (!list.containsValue(false)) {

                if (isNotEmptyRefills) {
                    addRows(
                        requireContext(), viewBinding.tablePageStatistic,
                        R.layout.table_row_statistic, refillsData/*.filterNotNull()*/, gasStationsData/*.filterNotNull()*/
                    )
                }
            }
        })

    }

    override fun onDestroyView() {

        refillsLiveData.removeObservers(viewLifecycleOwner)
        gasStationLiveData.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }


    @SuppressLint("SetTextI18n")
    fun addRows(
        context: Context?,
        tableLayout: TableLayout,
        @LayoutRes tableRowRes: Int,
        refillsData: List<Refill>,
        gasStationsData: List<GasStation>,
    ) {
        tableLayout.removeAllViews()
        val inflater = LayoutInflater.from(context)
        for (row in gasStationsData) {
            val tableRow = inflater.inflate(tableRowRes, null) as TableRow
            val binding: TableRowStatisticBinding = TableRowStatisticBinding.bind(tableRow)

            binding.colTitleStatistic.text = row.title
            tableRow.tag = row.title

            refillsData.filter { it.idGasStation == row.id }.also { refills ->
                binding.colFirstValueStatistic.text = refills.count().toString()
                binding.colSecondValueStatistic.text = refills.map { it.liter }
                    .sum().toBigDecimal().setScale(2, RoundingMode.UP).toString()
            }
            tableLayout.addView(tableRow)
            tableLayout.dividerDrawable.alpha = 127
        }
    }
}