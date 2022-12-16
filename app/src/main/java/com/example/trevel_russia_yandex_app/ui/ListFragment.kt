package com.example.trevel_russia_yandex_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel
import com.example.trevel_russia_yandex_app.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    private val viewModel: MapViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

//        if (requireArguments().containsKey("title")) {
//            Toast.makeText(
//                requireContext(),
//                "${requireArguments().getString("title")}",
//                Toast.LENGTH_LONG
//            ).show()
//        }



        return binding.root
    }


}