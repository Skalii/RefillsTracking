package skalii.testjob.trackensure


import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).also {
        it.setGravity(Gravity.BOTTOM, 0, 600)
        it.show()
    }
}

fun RecyclerView.setVerticalDivider(divider: Int = R.drawable.sh_divider_accent, alpha: Int = 127) {
    addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            ResourcesCompat.getDrawable(context.resources, divider, null)?.let {
                it.alpha = alpha
                setDrawable(it)
            }
        }
    )
}

fun View.setVisibility(condition: Boolean) {
    visibility = if (condition) VISIBLE else GONE
}
