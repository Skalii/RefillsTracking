package skalii.testjob.trackensure.ui.fragment.base


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

import kotlinx.coroutines.CoroutineScope

import skalii.testjob.trackensure.helper.model.base.BaseModel
import skalii.testjob.trackensure.ui.adapter.base.BaseCommonAdapter
import skalii.testjob.trackensure.ui.adapter.base.BasePagedAdapter


abstract class BasePageFragment(@LayoutRes private val contentLayoutId: Int) :
    Fragment(contentLayoutId) {

    protected abstract val viewBinding: ViewBinding
    protected abstract val mainLaunch: CoroutineScope

    protected val dataProgressionLiveData = MutableLiveData<MutableMap<String, Boolean>>()

    protected abstract val dataLoadingProgressTags: MutableMap<String, Boolean>


    protected open fun <T> createRecycler(
        recyclerView: RecyclerView,
        adapter: BaseCommonAdapter,
        func: () -> T? = { null }
    ): RecyclerView {
        return recyclerView
    }

    protected open fun <T, Model : BaseModel> createRecycler(
        recyclerView: RecyclerView,
        adapter: BasePagedAdapter<Model>,
        func: () -> T? = { null }
    ): RecyclerView {
        return recyclerView
    }

    protected open fun logNav(onAction: String?) =
        Log.d("PAGE NAVIGATION", "${javaClass.simpleName}(${view?.id}).$onAction")

    open fun <Model> setObserverSingle(liveData: MutableLiveData<Model?>) =
        Observer<Model?> { liveData.postValue(it) }

    open fun <Model> setObserverSome(liveData: MutableLiveData<List<Model>>) =
        Observer<List<Model>> { liveData.postValue(it) }

    open fun <Model> setObserverPaging(liveData: MutableLiveData<PagedList<Model>>) =
        Observer<PagedList<Model>> { liveData.postValue(it) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        logNav("onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logNav("onCreate()")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logNav("onViewCreated()")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logNav("onActivityCreated()")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        logNav("onViewStateRestored()")
    }

    override fun onResume() {
        logNav("onResume()")
        super.onResume()
    }

    override fun onPause() {
        logNav("onPause()")
        super.onPause()
    }

    override fun onStop() {
        logNav("onStop()")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logNav("onSaveInstanceState()")
    }

    override fun onDestroyView() {
        logNav("onDestroyView()")

        dataLoadingProgressTags.clear()

        dataProgressionLiveData.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }

    override fun onDestroy() {
        logNav("onDestroy()")
        super.onDestroy()
    }

    override fun onDetach() {
        logNav("onDetach()")
        super.onDetach()
    }

}