package com.example.trevel_russia_yandex_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.adapter.OnItemClickListener
import com.example.trevel_russia_yandex_app.adapter.PointsListAdapter
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel
import com.example.trevel_russia_yandex_app.databinding.FragmentListBinding
import com.example.trevel_russia_yandex_app.model.PointModel
import com.example.trevel_russia_yandex_app.utils.KeyboardUtils

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    private lateinit
    var adapter: PointsListAdapter
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val itemDecor = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            setDrawable(resources.getDrawable(R.drawable.divider))
        }
        binding.rcView.addItemDecoration(itemDecor)

        adapter = PointsListAdapter(object : OnItemClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(
                    R.id.action_listFragment_to_mapFragment,
                    Bundle().apply {
                        putInt("id", id)
                    })
            }

            override fun delete(id: Int) {

                viewModel.deletePoint(id)
            }

            override fun edit(id: Int) {
                val pointModel = viewModel.pointListLiveData.value?.filter { it.id == id }?.first()
                with(binding) {
                    createPointGroup.visibility = View.VISIBLE
                    inputTitle.setText(pointModel?.title)
                    inputContent.setText(pointModel?.content)
                }
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

        viewModel.pointListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.pointListLiveData.value?.map { it.toPointModel() })
        }

        with(binding) {
            buttonCreate.setOnClickListener {
                val pointModel = viewModel.pointListLiveData.value?.filter { it.id == id }?.last()
                viewModel.editPoint(pointModel?.id!!, pointModel.title , pointModel.content ?: "")
                KeyboardUtils.hideKeyboard(it)

            }

            buttonBack.setOnClickListener {
                createPointGroup.visibility = View.GONE
                KeyboardUtils.hideKeyboard(it)
            }
        }




    }


}