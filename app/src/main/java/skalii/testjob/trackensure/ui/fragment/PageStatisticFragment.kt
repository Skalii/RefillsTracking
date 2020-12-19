package skalii.testjob.trackensure.ui.fragment


import by.kirich1409.viewbindingdelegate.viewBinding

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.PageRefillBinding
import skalii.testjob.trackensure.ui.fragment.base.BasePageFragment


class PageStatisticFragment : BasePageFragment(R.layout.page_statistic) {

    override val viewBinding by viewBinding(PageRefillBinding::bind)

    override val dataLoadingProgressTags = mutableMapOf<String, Boolean>()

}