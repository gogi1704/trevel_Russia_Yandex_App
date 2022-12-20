package com.example.trevel_russia_yandex_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.adapter.OnItemClickListener
import com.example.trevel_russia_yandex_app.adapter.PointsListAdapter
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel
import com.example.trevel_russia_yandex_app.databinding.FragmentListBinding
import com.example.trevel_russia_yandex_app.model.PointModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    private lateinit var adapter: PointsListAdapter
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        adapter = PointsListAdapter(object : OnItemClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(
                    R.id.action_listFragment_to_mapFragment,
                    Bundle().apply {
                        putInt("id", id)
                    })
            }

        })

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_listFragment_to_mapFragment)
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcView.adapter = adapter
        adapter.submitList(viewModel.pointListLiveData.value?.map { it.toPointModel() })

    }


}