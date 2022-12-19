package com.example.trevel_russia_yandex_app.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.model.PointModel
import com.example.trevel_russia_yandex_app.viewModels.MapViewModel

class MyDialogFragment() : DialogFragment() {
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val point = viewModel.pointLiveData.value
        viewModel.emptyPointModel = PointModel(-1, "", "", 0.0, 0.0)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(point?.title)
                .setMessage(point?.content)
                .setIcon(R.drawable.ic_baseline_location_on_24)
                .setPositiveButton("Ok") { dialog, id ->
                    dialog.cancel()
                }
                .setNegativeButton("To points list") { dialog, _ ->
                    findNavController().navigate(R.id.action_mapFragment_to_listFragment)
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
