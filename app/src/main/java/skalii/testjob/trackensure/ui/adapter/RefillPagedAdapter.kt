package skalii.testjob.trackensure.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ItemRefillBinding
import skalii.testjob.trackensure.helper.getDateTimeFormatter
import skalii.testjob.trackensure.helper.model.GasStation
import skalii.testjob.trackensure.helper.model.Refill
import skalii.testjob.trackensure.helper.model.Supplier
import skalii.testjob.trackensure.helper.model.base.BaseModel.Companion.findById
import skalii.testjob.trackensure.ui.adapter.base.BasePagedAdapter
import skalii.testjob.trackensure.ui.adapter.base.BaseViewHolder


@Suppress("EXPERIMENTAL_API_USAGE")
class RefillPagedAdapter : BasePagedAdapter<Refill>() {

    private val gasStations = mutableListOf<GasStation>()
    private val suppliers = mutableListOf<Supplier>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RefillViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_refill, parent, false)
        )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        holder as RefillViewHolder

        getItem(position)?.let { refill ->
            holder.bind(
                refill,
                gasStations.findById(refill.idGasStation),
                suppliers.findById(refill.idSupplier)
            )
        }
    }

    fun setDataGasStations(data: List<GasStation>) = setData(data, gasStations)
    fun setDataSuppliers(data: List<Supplier>) = setData(data, suppliers)
    override fun clearData() {
        gasStations.clear()
        suppliers.clear()

        notifyDataSetChanged()
    }


    inner class RefillViewHolder(view: View) : BaseViewHolder(view) {

        override val viewBinding = ItemRefillBinding.bind(view)

        private val card = viewBinding.itemRefillCard
        private val textGasStation = viewBinding.textGasStationItemRefill
        private val textFuelSupplier = viewBinding.textFuelSupplierItemRefill
        private val textDate = viewBinding.textDateItemRefill
        private val textFuelType = viewBinding.textFuelTypeItemRefill
        private val textLiter = viewBinding.textLiterItemRefill
        private val textCost = viewBinding.textCostItemRefill

        fun bind(
            refill: Refill,
            gasStation: GasStation?,
            supplier: Supplier?
        ) {

            textGasStation.text = gasStation?.title
            textFuelSupplier.text = supplier?.name
            textDate.text = refill.date.format(getDateTimeFormatter())
            textFuelType.text = refill.fuelType.value
            textLiter.text = refill.liter.toString()
            textCost.text = refill.cost.toString()

        }

    }

}