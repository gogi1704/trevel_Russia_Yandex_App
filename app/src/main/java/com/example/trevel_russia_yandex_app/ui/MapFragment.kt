package com.example.trevel_russia_yandex_app.ui

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.constans.MAP_API_KEY
import com.example.trevel_russia_yandex_app.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MapFragment : Fragment(), GeoObjectTapListener, InputListener {
    private lateinit var map: Map
    private lateinit var binding: FragmentMapBinding
    private lateinit var iconPlaceMark: Bitmap
    private lateinit var collection: MapObjectCollection
    private val viewModel: MapViewModel by activityViewModels()
    private val dialog = MyDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAP_API_KEY)
        MapKitFactory.initialize(requireContext())
        iconPlaceMark =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24)
                ?.toBitmap()!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        map = binding.mapView.map.apply {
            addTapListener(this@MapFragment)
            addInputListener(this@MapFragment)
        }

        collection = map.mapObjects.addCollection()




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonList.setOnClickListener {
                findNavController().navigate(R.id.action_mapFragment_to_listFragment)
            }

            buttonCreate.setOnClickListener {
                val title = inputTitle.text.toString()
                val content = inputContent.text.toString()
                if (title != "") {
                    viewModel.createPlaceMark(

                        collection,
                        viewModel.emptyPoint,
                        title,
                        content,
                        iconPlaceMark
                    )
                }
                viewModel.emptyPoint = Point()
                createPointGroup.visibility = View.GONE
            }

            buttonBack.setOnClickListener {
                inputContent.text = null
                inputTitle.text = null
                createPointGroup.visibility = View.GONE
            }
        }

        viewModel.pointLiveData.observe(viewLifecycleOwner) {
            if (it.id != -1) {
                dialog.show(parentFragmentManager, "")
            }
        }

        viewModel.pointListLiveData.observe(viewLifecycleOwner){
            viewModel.addAllPlaceMarks(collection)
        }


    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onObjectTap(tap: GeoObjectTapEvent): Boolean {
        return true
    }

    override fun onMapTap(map: Map, point: Point) {
    }

    override fun onMapLongTap(map: Map, point: Point) {
        viewModel.emptyPoint = point
        binding.createPointGroup.visibility = View.VISIBLE
    }


}