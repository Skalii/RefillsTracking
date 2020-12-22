package skalii.testjob.trackensure.helper


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import java.time.format.DateTimeFormatter


fun Activity.isPermissionGranted(permission: String) =
    ActivityCompat.checkSelfPermission(applicationContext, permission) ==
            PackageManager.PERMISSION_GRANTED

fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }

    /*} else {
        (connectivityManager.activeNetworkInfo ?: return false).isConnected
    }*/
}

fun Context.toast(message: CharSequence) {
    Toast.makeText(this@toast, message, Toast.LENGTH_SHORT).also {
        it.setGravity(Gravity.BOTTOM, 0, 600)
        it.show()
    }
}

fun RecyclerView.setVerticalDivider(divider: Int, alpha: Int = 127) {
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

fun getDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")