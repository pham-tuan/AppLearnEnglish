package com.tuan.englishforkid.presentation.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import com.tuan.englishforkid.databinding.FragmentHomeBinding
import com.tuan.englishforkid.hideKeyBoard
import com.tuan.englishforkid.model.Topic
import com.tuan.englishforkid.model.TopicResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val viewModelTopic: HomeViewModel by viewModels()
    private lateinit var topicAdapter: TopicAdapter
    private val listTopic: ArrayList<Topic> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        binding.homeviewModel = viewModelTopic
        // viewModelTopic.getListTopic()
        setUpRecyclerview()
        setUpObserver()
        setUpSearchEdt()
        return binding.root
    }


    private fun setUpRecyclerview() {
        topicAdapter = TopicAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcTopic.apply {
            adapter = topicAdapter
            setLayoutManager(layoutManager)
        }

    }
// tìm kieesm
    private fun setUpSearchEdt() {
        binding.edtseach.doOnTextChanged{text ,_,_,_ ->
            val search = text.toString().toLowerCase()
            if(search.isEmpty()){
                showShimmer(true)
                topicAdapter.submitList(ArrayList(listTopic))
            }else{
                val filter = listTopic.filter {
                    it.title?.toLowerCase()?.contains(search) == true
                }
                showShimmer(false)
                topicAdapter.submitList(ArrayList(filter))
            }
        }
    }

    private fun setUpObserver() {
        viewModelTopic.getListTopic().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    showShimmer(false)
//                    val listTopic: ArrayList<Topic> = ArrayList()
                    val value = data.data?.body() as TopicResponse
                    listTopic.clear()
                    value.listTopic?.forEach {
                        listTopic.add(it)
                        Log.d("setUpObserver", listTopic.toString())
                    }

                    topicAdapter.submitList(listTopic)

                    topicAdapter.onItemClick = { topic ->
                        findNavController().navigate(
                            R.id.action_HomeFragment_to_DetailFragment, bundleOf(
                                Constant.KEY_DATA to topic
                            )
                        )
                    }
                }

                DataResult.Status.LOADING -> {
                    showShimmer(true)
                }

                DataResult.Status.ERROR -> {
                    showShimmer(false)
                }
            }
        }
    }

    private fun showShimmer(isShow: Boolean) {

        if (isShow) {
            binding.shimmer.visibility = View.VISIBLE
            binding.rcTopic.visibility = View.GONE
            binding.shimmer.startShimmer()

        } else {

            binding.shimmer.visibility = View.GONE
            binding.rcTopic.visibility = View.VISIBLE
            binding.shimmer.stopShimmer()

        }

    }

}