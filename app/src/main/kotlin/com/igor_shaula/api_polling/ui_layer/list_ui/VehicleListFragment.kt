package com.igor_shaula.api_polling.ui_layer.list_ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.toVehicleRecordList
import com.igor_shaula.api_polling.databinding.FragmentVehiclesListBinding
import com.igor_shaula.api_polling.ui_layer.SharedViewModel
import com.igor_shaula.api_polling.ui_layer.dialogs_ui.DetailFragment
import com.igor_shaula.api_polling.ui_layer.list_ui.all_for_list.VehicleListAdapter
import com.igor_shaula.utilities.AnimatedStringProgress
import timber.log.Timber

class VehicleListFragment : Fragment() {

    private lateinit var binding: FragmentVehiclesListBinding

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var rvAdapter: VehicleListAdapter

    private var animatedStringProgress: AnimatedStringProgress? = null

    // region standard lifecycle androidx.fragment.app.Fragment callbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView - 6")
        binding = FragmentVehiclesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated - 7")
        showNearVehiclesNumber()
        prepareVehiclesListUI()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        Timber.v("onStart - 10")
        viewModel.vehiclesMap.observe(viewLifecycleOwner) { thisMap ->
            animatedStringProgress?.stopShowingDynamicDottedText()
            prepareUIForListWithDetails(thisMap.toList())
        }
        viewModel.mainErrorStateInfo.observe(viewLifecycleOwner) { pair ->
            if (pair.second) {
                binding.actvErrorStateInfo.text = pair.first
            } else {
                Timber.v("mainErrorStateInfo: HIDDEN, message is: ${pair.first}")
            }
        }
        viewModel.timeToUpdateVehicleStatus.observe(viewLifecycleOwner) {
            viewModel.vehiclesMap.value?.toList()?.let {
                updateItemsInTheList(it)
                showNearVehiclesNumber()
            }
        }
        viewModel.timeToShowGeneralBusyState.observe(viewLifecycleOwner) { show ->
            binding.acivAlertIconForFakeData.isVisible = false
            showCentralBusyState(show)
        }
        viewModel.timeToAdjustForFakeData.observe(viewLifecycleOwner) {
            adjustTheBottomButtonForFakeData()
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume - 11")
        binding.actbPolling.setOnCheckedChangeListener { _, isChecked ->
            binding.actbPolling.textOff = getString(R.string.toggle_button_off_text)
            binding.actbPolling.textOn = getString(R.string.toggle_button_on_text)
            if (isChecked) {
                viewModel.startGettingVehiclesDetails()
            } else {
                viewModel.stopGettingVehiclesDetails()
            }
        }
        binding.acbLaunchInitialRequest.setOnClickListener {
            animatedStringProgress = AnimatedStringProgress(binding.acbLaunchInitialRequest)
            animatedStringProgress?.startShowingDynamicDottedText()
            viewModel.getAllVehiclesIds()
        }
        binding.acbRepeatInitialRequest.setOnClickListener {
            animatedStringProgress = AnimatedStringProgress(binding.acbRepeatInitialRequest)
            animatedStringProgress?.startShowingDynamicDottedText()
            viewModel.getAllVehiclesIds()
            hideErrorViewsDuringAnotherTryAttempt()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopGettingVehiclesDetails()
        viewModel.clearPreviousFakeDataSelection()
    }

    // endregion standard androidx.fragment.app.Fragment callbacks

    // region work with the List

    private fun prepareVehiclesListUI() {
        rvAdapter = VehicleListAdapter { vehicleItemRecord ->
            DetailFragment.show(childFragmentManager, vehicleItemRecord.vehicleId)
        }
        binding.rvVehiclesList.adapter = rvAdapter
        binding.rvVehiclesList.layoutManager = LinearLayoutManager(context)
    }

    private fun updateItemsInTheList(pairs: List<Pair<String, VehicleRecord>>) {
        val vehicleRecordsList = pairs.toVehicleRecordList()
        rvAdapter.setItems(vehicleRecordsList)
    }

    // endregion work with the List

    // region work with the rest of UI

    private fun showNearVehiclesNumber() {
        binding.actvHeader.text = getString(
            R.string.close_distance_counter_text_base,
            viewModel.getNumberOfNearVehicles(),
            viewModel.getNumberOfAllVehicles()
        )
    }

    private fun prepareUIForListWithDetails(list: List<Pair<String, VehicleRecord>>) {
        binding.groupInitial.isVisible = false
        if (list.isEmpty()) {
            binding.groupWithProperList.isVisible = false
            binding.groupWithAbsentList.isVisible = true
            binding.acivAlertIcon.isVisible = true
            binding.actvErrorStateInfo.isVisible = true
            binding.acbRepeatInitialRequest.isEnabled = true
        } else {
            binding.groupWithProperList.isVisible = true
            binding.groupWithAbsentList.isVisible = false
            updateItemsInTheList(list)
            showNearVehiclesNumber()
            binding.actbPolling.isEnabled = true
        }
    }

    private fun hideErrorViewsDuringAnotherTryAttempt() {
        binding.acivAlertIcon.isVisible = false
        binding.actvErrorStateInfo.isVisible = false
        binding.actvErrorStatePhrase.isVisible = false
    }

    private fun showCentralBusyState(isBusy: Boolean) {
        binding.pbCentral.isVisible = isBusy
    }

    private fun adjustTheBottomButtonForFakeData() {
        binding.acivAlertIcon.isVisible = false
        binding.acivAlertIconForFakeData.isVisible = true
        binding.actvErrorStateInfo.isVisible = false
        binding.actvErrorStatePhrase.text = getString(R.string.error_state_fake_ready_text)
        binding.acbRepeatInitialRequest.text = getString(R.string.error_state_use_fake_text)
    }

    // endregion work with the rest of UI
}
