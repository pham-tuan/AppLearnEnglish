package com.tuan.englishforkid.presentation.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentInforBinding


class InForFragment : Fragment() {

    private lateinit var binding: FragmentInforBinding
    private var pagerAdapter :  ViewPagerAdapter? =null
    private var listTitle : ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).handleShowHeader(true)  //ẩn header menu
        binding = FragmentInforBinding.inflate(inflater, container, false)
        setupviewPager()
        addTabLayout()
        return binding.root
    }

    private fun addTabLayout() {
        listTitle.add("Profile")
        listTitle.add("Edit")

        TabLayoutMediator(binding.tablayout , binding.vpprofile, object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = listTitle.getOrNull(position)
            }
        }) .attach()
    }

    private fun setupviewPager() {
        pagerAdapter = activity.let { ViewPagerAdapter(it,listTitle) }
        binding.vpprofile.adapter = pagerAdapter

        // chặn vuốt chuyển page
        binding.vpprofile.isUserInputEnabled = false
    }


}