package com.tuan.englishforkid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast


class BroadcastCheckInternet : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkConnected(context)) {
            Toast.makeText(context, "Mạng không khả dụng", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Đã có mạng ", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}