package com.tuan.englishforkid.presentation.detail1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.databinding.FragmentDetailBinding
import com.tuan.englishforkid.ext.hideLoading
import com.tuan.englishforkid.ext.showLoading
import com.tuan.englishforkid.model.Detail
import com.tuan.englishforkid.model.DetailResponse
import com.tuan.englishforkid.model.Topic
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private val viewModelDetail: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private val listDetail: ArrayList<Detail> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).handleShowHeader(false)
        binding = DataBindingUtil.inflate(inflater, com.tuan.englishforkid.R.layout.fragment_detail, container, false)
        binding.viewPagerDetail.isUserInputEnabled = false     // set không vuốt viewpager
        binding.lifecycleOwner = this
        binding.detailviewModel = viewModelDetail
        setupviewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Topic>(Constant.KEY_DATA)?.let {
            setUpObserver(it)
            setupviewPager()
        }

    }

    private fun setupviewPager() {
        detailAdapter = DetailAdapter()
        // Gán Adapter cho ViewPager2
        binding.viewPagerDetail.adapter = detailAdapter

        binding.tvNext.setOnClickListener {
            var indexItem = binding.viewPagerDetail.currentItem // Lấy vị trí hiện tại
            //lấy id của giá trị vị trí
            var status = listDetail[indexItem]
            var idindex = status.iddata

            if (binding.viewPagerDetail.currentItem + 1 < detailAdapter.itemCount) {
                binding.viewPagerDetail.currentItem += 1
                Log.d("ItemCount", detailAdapter.currentList.toString())
                if (idindex != null) {
                    viewModelDetail.putIdData(idindex).observe(viewLifecycleOwner) { putDataResult ->
                        when (putDataResult.status) {
                            DataResult.Status.SUCCESS -> {
                                // Xử lý khi putIdData thành công
                                Log.d("putIdData", "setupviewPager: put ${idindex} lên server thành công")
                            }
                            DataResult.Status.ERROR -> {
                                // Xử lý khi PUT request không thành công
                                Log.e("putIdData", "setupviewPager: lỗi PUT request - ${putDataResult.message ?: "Không rõ lỗi"}")
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                Toast.makeText(activity, "HẾT GÒI", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewPagerDetail.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        binding.tvExit.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setUpObserver(it: Topic) {
        binding.title.text = it.title
        viewModelDetail.getListDetail(it.type ?:"").observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    hideLoading()
                    val value = data.data?.body() as DetailResponse
                    value.listDetail?.forEach {
                        listDetail.add(it)
                        Log.d("listDetail", listDetail.toString())
                    }
                //    val listDetail = data.data?.body()?.listDetail ?: emptyList<Detail>()
                    // Cập nhật dữ liệu trong Adapter
                    detailAdapter.submitList(listDetail)
                }
                DataResult.Status.LOADING -> {
                   showLoading()
                }
                DataResult.Status.ERROR -> {
                    hideLoading()
                    Toast.makeText(context, "ERROR_API", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
