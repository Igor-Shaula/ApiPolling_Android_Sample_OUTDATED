package com.igor_shaula.api_polling.ui.detail_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data.VehicleDetailsRecord
import com.igor_shaula.api_polling.databinding.FragmentDetailBinding
import timber.log.Timber

class DetailFragment : DialogFragment() {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var viewModel: DetailViewModel

    private lateinit var vehicleId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { theBundle ->
            vehicleId = theBundle.getString(VEHICLE_ID_KEY).toString()
            Timber.d("vehicleId = $vehicleId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.vehiclesDetailsMap.observe(viewLifecycleOwner) {
            updateUI(it[vehicleId])
        }
    }

    private fun updateUI(vehicleDetailsRecord: VehicleDetailsRecord?) {
        binding.apply {
            actvCounterOfNearVehicles.text = getString(
                R.string.close_distance_counter_text_base, viewModel.getNumberOfNearVehicles()
            )
            actvVehicleId.text = getString(
                R.string.vehicleId, vehicleId
            )
            actvLiveStatus.text = getString(
                R.string.vehicleStatus, viewModel.vehiclesMap.value?.get(vehicleId).toString()
            )
            actvLiveCoordinates.text = getString(
                R.string.location_text_base,
                vehicleDetailsRecord?.latitude,
                vehicleDetailsRecord?.longitude
            )
        }
    }

    companion object {

        private const val DETAILS_FRAGMENT_TAG = "DetailFragment"
        private const val VEHICLE_ID_KEY = "vehicleId for this fragment"

        private fun newInstance(vehicleId: String) = DetailFragment().apply {
            arguments = Bundle(1).apply {
                putString(VEHICLE_ID_KEY, vehicleId)
            }
        }

        fun show(fragmentManager: FragmentManager, vehicleId: String) {
            if (fragmentManager.findFragmentByTag(DETAILS_FRAGMENT_TAG) == null) {
                newInstance(vehicleId).show(fragmentManager, DETAILS_FRAGMENT_TAG)
            }
        }
    }
}