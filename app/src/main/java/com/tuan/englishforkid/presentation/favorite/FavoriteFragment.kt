package com.tuan.englishforkid.presentation.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentDetailBinding
import com.tuan.englishforkid.databinding.FragmentFavoriteBinding
import com.tuan.englishforkid.model.Detail
import com.tuan.englishforkid.presentation.detail1.DetailAdapter
import com.tuan.englishforkid.presentation.detail1.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    private val viewModelDetail: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private val listDetail: ArrayList<Detail> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


}