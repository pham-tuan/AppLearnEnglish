package com.tuan.englishforkid.ext

import androidx.fragment.app.Fragment
import com.tuan.englishforkid.MainActivity

fun Fragment.showLoading() {
    (activity as MainActivity).apply {
        showLoading()
    }
}

fun Fragment.hideLoading() {
    (activity as MainActivity).apply {
        hideLoading()
    }
}