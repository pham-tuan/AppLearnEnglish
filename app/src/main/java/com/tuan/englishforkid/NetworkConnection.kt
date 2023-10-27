package com.tuan.englishforkid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkConnection {
    fun isNetworkAvailable(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        if (connMgr != null) {
            networkInfo = connMgr.activeNetworkInfo
        }
        return networkInfo != null && networkInfo.isConnected
    }
}
