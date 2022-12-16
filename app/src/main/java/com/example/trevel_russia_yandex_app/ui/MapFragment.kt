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


class MapFragment : Fragment(), GeoObjectTapListener, InputListener {
    private lateinit var map: Map
    private lateinit var binding: FragmentMapBinding
    private lateinit var iconPlaceMark: Bitmap
    private val viewModel: MapViewModel by activityViewModels()

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


        val point = Point(55.751574, 37.573856)

        map.move(
            CameraPosition(point, 16.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 2F),
            null
        )

        val collection = map.mapObjects.addCollection()
        viewModel.createPlaceMark(requireContext(), collection, point, "123213", iconPlaceMark)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonList.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_listFragment2)
            }

            buttonCreate.setOnClickListener {
                val title = inputTitle.text.toString()
                val content = inputContent.text.toString()

                findNavController().navigate(
                    R.id.action_listFragment_to_listFragment2,
                    Bundle().apply {
                        putString("title" , title)
                        putString("content" , content)
                    })
            }

            buttonBack.setOnClickListener {
                inputContent.text = null
                inputTitle.text = null
                createPointGroup.visibility = View.GONE
            }

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
        Toast.makeText(requireContext() , "${tap.geoObject.metadataContainer}" , Toast.LENGTH_LONG).show()
       return true
    }

    override fun onMapTap(map: Map, point: Point) {

    }

    override fun onMapLongTap(map: Map, point: Point) {

        binding.createPointGroup.visibility = View.VISIBLE
    }


}