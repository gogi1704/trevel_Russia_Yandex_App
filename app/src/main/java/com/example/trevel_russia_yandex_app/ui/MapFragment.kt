package com.example.trevel_russia_yandex_app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color.BLUE
import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.databinding.FragmentMapBinding
import com.example.trevel_russia_yandex_app.utils.KeyboardUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider.fromResource


class MapFragment : Fragment(), GeoObjectTapListener, InputListener, UserLocationObjectListener,
    CameraListener {
    private lateinit var map: Map
    private lateinit var binding: FragmentMapBinding
    private lateinit var iconPlaceMark: Bitmap
    private lateinit var collection: MapObjectCollection
    private lateinit var userLocationLayer: UserLocationLayer
    private var isStartPosition = false
    private var routeStartLocation = Point(0.0, 0.0)
    private val viewModel: MapViewModel by activityViewModels()
    private val dialog = MyMapDialogFragment()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onMapReady()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Sorry, but i need a grant!!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        checkPermissions()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            val point = viewModel.getPointById(requireArguments().getInt("id"))
            map.move(
                CameraPosition(point, 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2F),
                null
            )

        }

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
                inputContent.text = null
                inputTitle.text = null
                createPointGroup.visibility = View.GONE
                KeyboardUtils.hideKeyboard(binding.mapView)
            }

            buttonBack.setOnClickListener {
                inputContent.text = null
                inputTitle.text = null
                createPointGroup.visibility = View.GONE
            }

            buttonGoToMe.setOnClickListener {
                cameraUserPosition()
            }
        }

        viewModel.pointLiveData.observe(viewLifecycleOwner) {
            if (it.id != -1) {
                dialog.show(parentFragmentManager, "")
            }
        }

        viewModel.pointListLiveData.observe(viewLifecycleOwner) {
            viewModel.addAllPlaceMarks(collection)
        }


    }

    private fun onMapReady() {

        val mapkit = MapKitFactory.getInstance()
        userLocationLayer = mapkit.createUserLocationLayer(binding.mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        map.addCameraListener(this)

    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                onMapReady()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }

    private fun cameraUserPosition() {
        if (userLocationLayer.cameraPosition() != null) {
            routeStartLocation = userLocationLayer.cameraPosition()!!.target
            map.move(
                CameraPosition(routeStartLocation, 16f, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 2F)
            ) { }
        } else {
            Toast.makeText(requireContext(), "Wait , and repeat!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setAnchor() {
        userLocationLayer.setAnchor(
            PointF(
                (binding.mapView.width * 0.5).toFloat(),
                (binding.mapView.height * 0.5).toFloat()
            ),
            PointF(
                (binding.mapView.width * 0.5).toFloat(),
                (binding.mapView.height * 0.83).toFloat()
            )
        )

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

    override fun onDestroyView() {
        super.onDestroyView()
        arguments?.clear()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {

        userLocationView.pin.setIcon(
            fromResource(
                requireContext(),
                R.drawable.ic_launcher_foreground
            )
        )
        userLocationView.arrow.setIcon(
            fromResource(
                requireContext(),
                R.drawable.ic_launcher_foreground
            )
        )
        userLocationView.accuracyCircle.fillColor = BLUE.alpha
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        finish: Boolean
    ) {

        if (finish) {
            if (isStartPosition) {
                cameraUserPosition()
                setAnchor()

            }

        }
    }
}