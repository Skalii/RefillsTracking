package skalii.testjob.trackensure.ui.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun containsItem(itemId: Long) =
        fragmentList.map { it.hashCode().toLong() }.contains(itemId)

    override fun createFragment(position: Int) = fragmentList[position]

    override fun getItemCount() = fragmentList.size

    override fun getItemId(position: Int) =
        if (position < 0) NO_ID else fragmentList[position].hashCode().toLong()

}