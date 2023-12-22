package com.igor_shaula.api_polling.ui_layer.list_ui

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.toVehicleRecordList
import com.igor_shaula.api_polling.databinding.FragmentVehiclesListBinding
import com.igor_shaula.api_polling.ui_layer.SharedViewModel
import com.igor_shaula.api_polling.ui_layer.detail_ui.DetailFragment
import com.igor_shaula.api_polling.ui_layer.list_ui.all_for_list.VehicleListAdapter
import timber.log.Timber

class VehicleListFragment : Fragment() {

    private lateinit var binding: FragmentVehiclesListBinding

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var rvAdapter: VehicleListAdapter

    // region standard lifecycle androidx.fragment.app.Fragment callbacks

    @Deprecated("Deprecated in Java")
    override fun onInflate(activity: Activity, attrs: AttributeSet, savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        super.onInflate(activity, attrs, savedInstanceState)
        Timber.v("onInflate deprecated")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        Timber.v("onInflate - 1")
    }

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        @Suppress("DEPRECATION")
        super.onAttach(activity)
        Timber.v("onAttach - 2 deprecated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.v("onAttach - 3")
        // java.lang.IllegalStateException: Can't access ViewModels from detached fragment
//        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate - 4")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView - 5")
        binding = FragmentVehiclesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated - 6")
        showNearVehiclesNumber()
        prepareVehiclesListUI()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        super.onActivityCreated(savedInstanceState)
        Timber.v("onActivityCreated - 7 deprecated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.v("onViewStateRestored - 8")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        Timber.v("onStart - 9")
        viewModel.vehiclesMap.observe(viewLifecycleOwner) { thisMap ->
            prepareUIForListWithDetails(thisMap.toList())
        }
        viewModel.timeToUpdateVehicleStatus.observe(viewLifecycleOwner) {
            viewModel.vehiclesMap.value?.toList()?.let {
                updateItemsInTheList(it)
                showNearVehiclesNumber()
            }
        }
        viewModel.timeToShowGeneralBusyState.observe(viewLifecycleOwner) { show ->
            showCentralBusyState(show)
        }
        viewModel.getAllVehiclesIds() // first data fetch which is one-time due to the requirements
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume - 10")
        binding.actbPolling.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.startGettingVehiclesDetails()
            else viewModel.stopGettingVehiclesDetails()
        }
        binding.acbLaunchInitialRequest.setOnClickListener {
            viewModel.getAllVehiclesIds()
            hideErrorViewsDuringFirstRequest()
            showCentralBusyState(true)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopGettingVehiclesDetails()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.v("onSaveInstanceState - 11")
    }

    // endregion standard androidx.fragment.app.Fragment callbacks

    // region other standard androidx.fragment.app.Fragment callbacks

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.v("onConfigurationChanged")
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        Timber.v("onGetLayoutInflater")
        return super.onGetLayoutInflater(savedInstanceState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.v("onHiddenChanged")
    }

    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
        Timber.v("onPrimaryNavigationFragmentChanged")
    }

    // endregion other standard androidx.fragment.app.Fragment callbacks

    // region work with the List

    private fun prepareVehiclesListUI() {
        rvAdapter = VehicleListAdapter { vehicleItemRecord ->
            DetailFragment.show(childFragmentManager, vehicleItemRecord.vehicleId)
        }
        binding.rvVehiclesList.adapter = rvAdapter
        binding.rvVehiclesList.layoutManager = LinearLayoutManager(context)
    }

    private fun updateItemsInTheList(pairs: List<Pair<String, VehicleStatus>>) {
        val vehicleRecordsList = pairs.toVehicleRecordList()
        rvAdapter.setItems(vehicleRecordsList)
    }

    // endregion work with the List

    private fun showNearVehiclesNumber() {
        val howManyVehiclesAreNear = viewModel.getNumberOfNearVehicles()
        binding.actvHeader.text =
            getString(R.string.close_distance_counter_text_base, howManyVehiclesAreNear)
    }

    private fun prepareUIForListWithDetails(list: List<Pair<String, VehicleStatus>>) {
        if (list.isEmpty()) {
            binding.groupWithProperList.isVisible = false
            binding.groupWithAbsentList.isVisible = true
            binding.acbLaunchInitialRequest.isEnabled = true
        } else {
            binding.groupWithProperList.isVisible = true
            binding.groupWithAbsentList.isVisible = false
            updateItemsInTheList(list) // now it's time to get all details for every item in the list
            showNearVehiclesNumber()
        }
    }

    private fun hideErrorViewsDuringFirstRequest() {
        binding.acivAlert.isVisible = false
        binding.actvErrorStatePhrase.isVisible = false
        binding.acbLaunchInitialRequest.isEnabled = false
    }

    private fun showCentralBusyState(isBusy: Boolean) {
        binding.pbCentral.isVisible = isBusy
    }
}
