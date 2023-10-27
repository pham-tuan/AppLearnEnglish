package com.tuan.englishforkid.presentation.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: FragmentActivity?, private var listTitle: ArrayList<String>) :
    FragmentStateAdapter(fragment!!) {

    override fun getItemCount(): Int = listTitle.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShowProfileFragment()
            1 -> ChangeProfileFragment()
            else -> ShowProfileFragment()
        }
    }

}