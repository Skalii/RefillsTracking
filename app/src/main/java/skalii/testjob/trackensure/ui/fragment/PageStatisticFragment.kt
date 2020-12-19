package skalii.testjob.trackensure.ui.fragment


import by.kirich1409.viewbindingdelegate.viewBinding

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.PageRefillBinding
import skalii.testjob.trackensure.ui.fragment.base.BasePageFragment


class PageStatisticFragment : BasePageFragment(R.layout.page_statistic) {

    override val viewBinding by viewBinding(PageRefillBinding::bind)
    override val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    override val dataLoadingProgressTags = mutableMapOf<String, Boolean>()

}